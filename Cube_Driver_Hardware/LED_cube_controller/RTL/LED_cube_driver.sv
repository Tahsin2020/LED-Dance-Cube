module LED_cube_driver(
    // basic signals
    input logic clk,
    input logic rst_n,
    input logic [7:0] uart_in,
    input logic [9:0] SW,

    //output signals
    output logic [7:0] Layers_out, 
    output logic [7:0] Latches_out, 
    output logic [7:0] Data_out,

    output logic [3:0] mode
);

    logic [7:0] multi_frame_layers, multi_frame_latches, multi_frame_data;
    assign {Layers_out, Latches_out, Data_out} = {multi_frame_layers, multi_frame_latches, multi_frame_data};

    // logic [3:0] mode, brightness, animation_sel;
    logic [3:0] brightness, animation_sel;

    LED_cube_multi_frame frames_driver(
        .clk(clk),
        .rst_n(rst_n),
        .animate_start(mode[0]),
        .animate_stop(~mode[0]),

        .animation_sel(animation_sel[2:0]),
        .mode(mode),
        .brightness(brightness),
        .SW(SW),


        .Layers(multi_frame_layers),
        .Latches(multi_frame_latches),
        .Data(multi_frame_data)
    );

    Config_Space conf_space(
        .clk(clk),
        .rst_n(rst_n),
        .uart_in(uart_in),
        .brightness(brightness),
        .mode(mode),
        .animation_sel(animation_sel)
    );

endmodule : LED_cube_driver