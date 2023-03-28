
module Cube_controller (
	clk_clk,
	reset_reset_n,
	uart_0_external_connection_rxd,
	uart_0_external_connection_txd,
	led_cube_uart_0_conduit_end_ledr,
	led_cube_uart_0_conduit_end_sw,
	led_cube_uart_0_conduit_end_gpio);	

	input		clk_clk;
	input		reset_reset_n;
	input		uart_0_external_connection_rxd;
	output		uart_0_external_connection_txd;
	output	[7:0]	led_cube_uart_0_conduit_end_ledr;
	input	[9:0]	led_cube_uart_0_conduit_end_sw;
	output	[35:0]	led_cube_uart_0_conduit_end_gpio;
endmodule
