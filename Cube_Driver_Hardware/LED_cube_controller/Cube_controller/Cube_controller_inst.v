	Cube_controller u0 (
		.clk_clk                          (<connected-to-clk_clk>),                          //                         clk.clk
		.led_cube_uart_0_conduit_end_ledr (<connected-to-led_cube_uart_0_conduit_end_ledr>), // led_cube_uart_0_conduit_end.ledr
		.led_cube_uart_0_conduit_end_sw   (<connected-to-led_cube_uart_0_conduit_end_sw>),   //                            .sw
		.led_cube_uart_0_conduit_end_gpio (<connected-to-led_cube_uart_0_conduit_end_gpio>), //                            .gpio
		.led_cube_uart_0_conduit_end_hex0 (<connected-to-led_cube_uart_0_conduit_end_hex0>), //                            .hex0
		.led_cube_uart_0_conduit_end_hex1 (<connected-to-led_cube_uart_0_conduit_end_hex1>), //                            .hex1
		.reset_reset_n                    (<connected-to-reset_reset_n>),                    //                       reset.reset_n
		.uart_0_external_connection_rxd   (<connected-to-uart_0_external_connection_rxd>),   //  uart_0_external_connection.rxd
		.uart_0_external_connection_txd   (<connected-to-uart_0_external_connection_txd>)    //                            .txd
	);

