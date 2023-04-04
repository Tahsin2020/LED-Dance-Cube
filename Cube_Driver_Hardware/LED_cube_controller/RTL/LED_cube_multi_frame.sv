//`define TB

`ifdef TB
	`define frame_time 21'hff
`else 
	`define frames_per_animation 8'd149
	`define frame_time 21'b101101110001101100000
	`define loops_per_animation 3'd4
	`define num_animations 3'd6
`endif
	
	
module LED_cube_multi_frame(
	// basic signals
	input logic clk,
	input logic rst_n,
	input logic animate_start,
	input logic animate_stop,
	input logic readdatavalid,
	
	// config signals
	input logic [3:0] mode,
	input logic [3:0] brightness,
	input logic [3:0] animation_sel,
	input logic loop_mode,

	// in_data
	input logic [7:0] data_in,

	// outputs
	output logic [7:0] Layers,
	output logic [7:0] Latches,
	output logic [7:0] Data,

	output logic stall_mode_change
);
	
	logic frame_start, frame_stop, frame_done;
	logic [7:0] data_to_latch, stream_data_to_latch;
	logic [5:0] frame_addr;
	logic [7:0] offset, stream_offset;
	logic [20:0] frame_timer;

	enum bit[1:0] {WAIT, DRIVE_FRAME, NEXT_FRAME} state, next_state;

	always_ff @( posedge clk ) begin : state_seq_logic
		if( ~rst_n ) state <= WAIT;
		else begin
			state <= next_state;
		end	
	end
	
	always_comb begin : next_state_logic
		next_state = state;
		if(animate_stop) next_state = WAIT;
		else begin
			case(state)
			WAIT: if(animate_start) next_state = DRIVE_FRAME;
				DRIVE_FRAME: if(timer_done) next_state = NEXT_FRAME;
				NEXT_FRAME: next_state = DRIVE_FRAME;
			endcase
		end
	end
	
	logic [7:0] data1 [9599:0];
	logic [7:0] data2 [9599:0];
	logic [7:0] data3 [9599:0];
	logic [7:0] data4 [9599:0];
	logic [7:0] data5 [9599:0];
	logic [7:0] data6 [9599:0];
	logic [7:0] data7 [9599:0];
	
	initial begin
	$readmemh("../Animations/hexes/diamond.hex", data1);
	$readmemh("../Animations/hexes/hexecone.hex", data2);
	$readmemh("../Animations/hexes/pulsating_sphere.hex", data3);
	$readmemh("../Animations/hexes/rolling_ball.hex", data4);
	$readmemh("../Animations/hexes/rotating_wall.hex", data5);
	$readmemh("../Animations/hexes/waves.hex", data6);
	$readmemh("../Animations/hexes/helix.hex", data7);
	end

	logic [13:0] addr;
	assign addr = {offset, frame_addr};

	logic [2:0] animation_offset;

	always_comb begin : pick_animation_block
		data_to_latch = 0;
		case(mode)
			4'h1: case(animation_offset)
					3'b000: data_to_latch = data1[addr];
					3'b001: data_to_latch = data2[addr];
					3'b010: data_to_latch = data3[addr];
					3'b011: data_to_latch = data4[addr];
					3'b100: data_to_latch = data5[addr];
					3'b101: data_to_latch = data6[addr];
					3'b110: data_to_latch = data7[addr];
					default: data_to_latch = 0;
				endcase
			4'h2: case(animation_sel[2:0])
					3'b001: data_to_latch = data1[addr];
					3'b010: data_to_latch = data2[addr];
					3'b011: data_to_latch = data3[addr];
					3'b100: data_to_latch = data4[addr];
					3'b101: data_to_latch = data5[addr];
					3'b110: data_to_latch = data6[addr];
					3'b111: data_to_latch = data7[addr];
					default: data_to_latch = 0;
				endcase
			4'h3: data_to_latch = stream_data_to_latch;
			// 4'h4: MODE PLANE MESSAGE to implement 
			// 4'h5: MODE DB ANIMATION to implement
			4'hf: data_to_latch = 8'hff;
			default: data_to_latch = 8'h0;
		endcase
	end

	logic timer_done;
	assign timer_done	= (frame_timer == `frame_time) ? 1'b1 : 1'b0;
	
	always_ff @( posedge clk ) begin : frame_timer_logic
		if( ~rst_n ) frame_timer <= 21'b0;
		else begin
			if(state != WAIT) begin
				if(~timer_done) frame_timer <= frame_timer + 1'b1;
				else frame_timer <= 0;
			end
		end
	end

	always_ff @( posedge clk ) begin : offset_logic
		if( ~rst_n ) offset <= 8'b0;
		else begin
			if(state == NEXT_FRAME) begin 
				if(offset != `frames_per_animation) offset <= offset + 1'b1;
				else offset <= 0;
			end
		end
	end
	
	logic [2:0] animation_loop_d, animation_loop_q;

	always_ff @( posedge clk) animation_loop_q <= animation_loop_d;

	always_comb begin
		animation_loop_d = animation_loop_q;
		if(~loop_mode) animation_loop_d = 3'b0;
		else if((offset == `frames_per_animation) && timer_done == 1'b1) begin
			if(animation_loop_q == `loops_per_animation) animation_loop_d = 3'd0;
			else animation_loop_d = animation_loop_q + 1'b1;
		end
	end

	always_ff @( posedge clk ) begin : animation_offset_blk
		if( ~rst_n | ~loop_mode) animation_offset <= 3'd0;
		else begin
			if(animation_loop_q == `loops_per_animation && animation_loop_d != animation_loop_q) begin
				if(animation_offset == `num_animations) animation_offset <= 3'd0;
				else animation_offset <= animation_offset + 1'b1;
			end
		end
	end

	always_comb begin : frame_start_logic
		frame_start = 1'b0;
		if(state != next_state && next_state == DRIVE_FRAME) frame_start = 1'b1;
	end
	
	assign frame_stop = animate_stop;
							
	LED_cube_single_frame frame_drive(
		.clk(clk),
		.rst_n(rst_n),
		.start(frame_start),
		.stop(frame_stop),
		.done(frame_done),
		.addr(frame_addr),
		.data_to_latch(data_to_latch),
		.Layers(Layers),
		.Latches(Latches),
		.Data(Data)
	);

	logic new_data;

	LED_cube_stream stream_data (
		.clk(clk),
		.rst_n(rst_n),
		.mode(mode),
		.new_data(new_data),
		.data_in(data_in),
		.frame_addr(frame_addr),
		.data_to_latch(stream_data_to_latch)
	);

	LED_cube_stream_controller stream_controller(
		.clk(clk),
		.rst_n(rst_n),
		.mode(mode),
		.data_in(data_in),
		.readdatavalid(readdatavalid),
		.new_data(new_data),
		.stall_mode_change(stall_mode_change)
	);

endmodule : LED_cube_multi_frame