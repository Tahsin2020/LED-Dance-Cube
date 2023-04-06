module LED_cube_controller (
        input logic CLOCK_50,
        input logic [3:0] KEY,
		output wire [9:0] LEDR,                         //   conduit_end.new_signal
        input logic [9:0] SW,
        inout logic [35:0] GPIO_0,
        inout logic [35:0] GPIO_1,
        output logic [6:0] HEX0,
        output logic [6:0] HEX1
);

   Cube_controller controller(
		.clk_clk(CLOCK_50),                                                 // clk_clk
		.led_cube_uart_0_conduit_end_ledr(LEDR[7:0]),                          
		.led_cube_uart_0_conduit_end_sw(SW),  
      .led_cube_uart_0_conduit_end_layers(Layers_out),                             //                                  .Latches
      .led_cube_uart_0_conduit_end_latches(Latches_out),                             //                                  .Layers
      .led_cube_uart_0_conduit_end_data(Data_out),                             //                                  .Data
		.reset_reset_n(KEY[0]),                                             //                             reset.reset_n
		.uart_0_external_connection_rxd(GPIO_0[24]),                        //        uart_0_external_connection.rxd
		.uart_0_external_connection_txd(GPIO_1[34]),                         // 
      .led_cube_uart_0_conduit_end_hex0(HEX0),
      .led_cube_uart_0_conduit_end_hex1(HEX1)
   );

   logic [7:0] Layers_out, Latches_out, Data_out;

   assign {GPIO_0[32], GPIO_0[30], GPIO_0[28], GPIO_0[26], GPIO_0[35], GPIO_0[33], GPIO_0[31], GPIO_0[27]} = Layers_out;
	assign {GPIO_0[25], GPIO_0[7], GPIO_0[9], GPIO_0[13], GPIO_0[15], GPIO_0[19], GPIO_0[21], GPIO_0[23]} = Latches_out;
	assign {GPIO_0[2], GPIO_0[4], GPIO_0[6], GPIO_0[10], GPIO_0[12], GPIO_0[16], GPIO_0[18], GPIO_0[20]} = Data_out;

   assign GPIO_1[33:0] = 34'b0;
	
	assign LEDR[9:8] = 1'b0;

endmodule : LED_cube_controller
