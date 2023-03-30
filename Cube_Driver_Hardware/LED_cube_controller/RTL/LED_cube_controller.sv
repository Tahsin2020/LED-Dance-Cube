module LED_cube_controller (
        input logic CLOCK_50,
        input logic [3:0] KEY,
		output wire [7:0] LEDR,                         //   conduit_end.new_signal
        input logic [9:0] SW,
        output logic [35:0] GPIO_0,
        inout logic [35:0] GPIO_1
);

   Cube_controller controller(
		.clk_clk(CLOCK_50),                                                 // clk_clk
		.led_cube_uart_0_conduit_end_ledr(LEDR[7:0]),                          
		.led_cube_uart_0_conduit_end_sw(SW),  
        .led_cube_uart_0_conduit_end_gpio(GPIO_0),                             //                                  .readdata
		.reset_reset_n(KEY[0]),                                             //                             reset.reset_n
		.uart_0_external_connection_rxd(GPIO_1[35]),                        //        uart_0_external_connection.rxd
		.uart_0_external_connection_txd(GPIO_1[34]),
		.KEY1(KEY[1])// 
   );

endmodule : LED_cube_controller
