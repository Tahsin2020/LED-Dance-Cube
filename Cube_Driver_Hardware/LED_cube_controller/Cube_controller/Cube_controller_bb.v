
module Cube_controller (
	clk_clk,
	led_cube_uart_0_conduit_end_ledr,
	led_cube_uart_0_conduit_end_sw,
	led_cube_uart_0_conduit_end_gpio,
	led_cube_uart_0_conduit_end_hex0,
	led_cube_uart_0_conduit_end_hex1,
	reset_reset_n,
	uart_0_external_connection_rxd,
	uart_0_external_connection_txd);	

	input		clk_clk;
	output	[7:0]	led_cube_uart_0_conduit_end_ledr;
	input	[9:0]	led_cube_uart_0_conduit_end_sw;
	output	[35:0]	led_cube_uart_0_conduit_end_gpio;
	output	[6:0]	led_cube_uart_0_conduit_end_hex0;
	output	[6:0]	led_cube_uart_0_conduit_end_hex1;
	input		reset_reset_n;
	input		uart_0_external_connection_rxd;
	output		uart_0_external_connection_txd;
endmodule
