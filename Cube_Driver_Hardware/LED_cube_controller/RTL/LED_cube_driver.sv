module LED_cube_driver(
    // basic signals
    input logic clk,
    input logic rst_n,
    input logic [7:0] uart_in,
    input logic readdatavalid,

    //output signals
    output logic [7:0] Layers_out, 
    output logic [7:0] Latches_out, 
    output logic [7:0] Data_out,

    output logic [3:0] mode,
    output logic [5:0] stream_data_counter
);

    enum bit[2:0] {OFF, ANIM_LOOP, ANIM_SEL, STREAM, PLANE_MSG, ALL_ON, ANIM_DB} state, next_state;

    // logic [3:0] mode, brightness, animation_sel;
    logic [3:0] brightness, animation_sel;

    always_ff @( posedge clk ) begin : state_seq_logic_blk
        if( ~rst_n ) state <= OFF;
        else state <= next_state;
    end

    always_comb begin : next_state_comb_logic_blk
        next_state = state;
        case(mode)
            4'd0: next_state = OFF;
            4'h1: next_state = ANIM_LOOP; 
            4'h2: next_state = ANIM_SEL;
            4'h3: next_state = STREAM;
            4'h4: next_state = PLANE_MSG;
            4'h5: next_state = ALL_ON;
            4'hF: next_state = ANIM_DB;
        endcase
    end

    logic [7:0] multi_frame_layers, multi_frame_latches, multi_frame_data;
    logic [7:0] stream_layers, stream_latches, stream_data;
    logic [7:0] plane_msg_layers, plane_msg_latches, plane_msg_data;
    logic [7:0] all_on_layers, all_on_latches, all_on_data;
    logic [7:0] anim_db_layers, anim_db_latches, anim_db_data;

    always_ff @( posedge clk )  begin : Layers_Latches_Data_comb_logic
        if( ~rst_n ) {Layers_out, Latches_out, Data_out} <= 0; 
        else {Layers_out, Latches_out, Data_out} <= {multi_frame_layers, multi_frame_latches, multi_frame_data};
    end

    logic loop_mode;
    assign loop_mode = (state == ANIM_LOOP) ? 1'b1 : 1'b0;

    logic multi_frame_animate_start, multi_frame_animate_stop;
    
    always_comb begin : multi_frmae_start_stop_logic
        multi_frame_animate_start = 1'b0;
        multi_frame_animate_stop  = 1'b0;
        if(state != next_state) begin
            if(next_state == ANIM_LOOP || next_state == ANIM_SEL 
            || next_state == STREAM || next_state == ALL_ON)
                multi_frame_animate_start = 1'b1;
            if(next_state == PLANE_MSG || next_state == ANIM_DB 
            || next_state ==  OFF)
                multi_frame_animate_stop = 1'b1;
        end
    end


    LED_cube_multi_frame frames_driver(
        .clk(clk),
        .rst_n(rst_n),
        .animate_start(multi_frame_animate_start),
        .animate_stop(multi_frame_animate_stop),

        .animation_sel(animation_sel[2:0]),
        .loop_mode(loop_mode),
        .mode(mode),
        .brightness(brightness),
        .data_in(uart_in),
        .readdatavalid(readdatavalid),


        .Layers(multi_frame_layers),
        .Latches(multi_frame_latches),
        .Data(multi_frame_data),

        .stall_mode_change(stall_mode_change),
        .stream_data_counter(stream_data_counter)
    );

    logic stall_mode_change;

    Config_Space conf_space(
        .clk(clk),
        .rst_n(rst_n),
        .uart_in(uart_in),
        .brightness(brightness),
        .mode(mode),
        .animation_sel(animation_sel),
        .stall_mode_change(stall_mode_change)
    );

endmodule : LED_cube_driver