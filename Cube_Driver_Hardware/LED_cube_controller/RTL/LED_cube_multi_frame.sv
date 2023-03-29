//`define TB

`define frames_per_animation 8'd149
`define loops_per_animation 4'd5

`ifdef TB
	`define frame_time 21'hff
`else 
	`define frame_time 21'b101101110001101100000
`endif
	
	
module LED_cube_multi_frame(
	// basic signals
	input logic clk,
	input logic rst_n,
	input logic animate_start,
	input logic animate_stop,
	
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
	output logic [7:0] Data
);
	
	logic frame_start, frame_stop, frame_done;
	logic [7:0] data_to_latch;
	logic [5:0] frame_addr;
	logic [7:0] offset;
	logic [20:0] frame_timer;

	enum bit[1:0] {WAIT, DRIVE_FRAME, NEXT_FRAME} state, next_state;
	
//	logic [63:0] [7:0] data = { 8'h81, 8'h42, 8'h24, 8'h18, 8'h18, 8'h24, 8'h42, 8'h81, 
//											8'h00, 8'h42, 8'h24, 8'h18, 8'h18, 8'h24, 8'h42, 8'h00, 
//											8'h00, 8'h00, 8'h24, 8'h18, 8'h18, 8'h24, 8'h00, 8'h00, 
//											8'h00, 8'h00, 8'h00, 8'h18, 8'h18, 8'h00, 8'h00, 8'h00, 
//											8'h00, 8'h00, 8'h00, 8'h18, 8'h18, 8'h00, 8'h00, 8'h00, 
//											8'h00, 8'h00, 8'h24, 8'h18, 8'h18, 8'h24, 8'h00, 8'h00, 
//											8'h00, 8'h42, 8'h24, 8'h18, 8'h18, 8'h24, 8'h42, 8'h00, 
//											8'h81, 8'h42, 8'h24, 8'h18, 8'h18, 8'h24, 8'h42, 8'h81  };

//	logic [9599:0] [7:0] data = {30{ 8'h81, 8'h42, 8'h24, 8'h18, 8'h18, 8'h24, 8'h42, 8'h81, 
//												8'h00, 8'h42, 8'h24, 8'h18, 8'h18, 8'h24, 8'h42, 8'h00, 
//												8'h00, 8'h00, 8'h24, 8'h18, 8'h18, 8'h24, 8'h00, 8'h00, 
//												8'h00, 8'h00, 8'h00, 8'h18, 8'h18, 8'h00, 8'h00, 8'h00, 
//												8'h00, 8'h00, 8'h00, 8'h18, 8'h18, 8'h00, 8'h00, 8'h00, 
//												8'h00, 8'h00, 8'h24, 8'h18, 8'h18, 8'h24, 8'h00, 8'h00, 
//												8'h00, 8'h42, 8'h24, 8'h18, 8'h18, 8'h24, 8'h42, 8'h00, 
//												8'h81, 8'h42, 8'h24, 8'h18, 8'h18, 8'h24, 8'h42, 8'h81, {64{8'b0} } }} ;

	logic [7:0] data1 [9599:0];
	logic [7:0] data2 [9599:0];
	logic [7:0] data3 [9599:0];
	logic [7:0] data4 [9599:0];
	logic [7:0] data5 [9599:0];
	
	initial begin
	$readmemh("../Animations/animation_rotating_wall.hex", data1);
	$readmemh("../Animations/animation_rotation.hex", data2);
	$readmemh("../Animations/Animation_squares.hex", data3);
	$readmemh("../Animations/animation_moving_cube.hex", data4);
	$readmemh("../Animations/animation_hi.hex", data5);
	end

	logic timer_done;
	assign timer_done	= (frame_timer == `frame_time) ? 1'b1 : 1'b0;
	
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
	
	always_ff @( posedge clk ) begin : offset_logic
		if( ~rst_n ) offset <= 8'b0;
		else if(state == NEXT_FRAME) begin 
			if(offset != `frames_per_animation) offset <= offset + 1'b1;
			else offset <= 0;
		end
	end
	
	always_ff @( posedge clk ) begin : frame_timer_logic
		if( ~rst_n ) frame_timer <= 21'b0;
		else begin
			if(state != WAIT) begin
				if(~timer_done) frame_timer <= frame_timer + 1'b1;
				else frame_timer <= 0;
			end
		end
	end
	
	logic [13:0] addr;
	assign addr = {offset, frame_addr};
	
	logic [3:0] animation_loop;

	always_ff @( posedge clk ) begin
		if( ~rst_n | ~loop_mode) animation_loop <= 4'b0;
		else if(offset == `frames_per_animation) begin
			animation_loop <= animation_loop + 1'b1;
		end
	end

	logic [2:0] animation_sel_loop;

	always_ff @( posedge clk ) begin : animation_sel_loop_seq_blk
		if( ~rst_n ) animation_sel_loop <= 0;
		else if( animation_loop == `loops_per_animation)
			animation_sel_loop <= animation_sel_loop + 1'b1;
	end

	always_comb begin : pick_animation_block
		case(animation_sel[2:0] + animation_sel_loop)
			3'b000: data_to_latch = data1[addr];
			3'b001: data_to_latch = data2[addr];
			3'b010: data_to_latch = data3[addr];
			3'b011: data_to_latch = data4[addr];
			3'b100: data_to_latch = data5[addr];
			default: data_to_latch = 0;
		endcase
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
		.addr( ),
		.data_to_latch(data_to_latch),
		.Layers(Layers),
		.Latches(Latches),
		.Data(Data)
	);

endmodule : LED_cube_multi_frame