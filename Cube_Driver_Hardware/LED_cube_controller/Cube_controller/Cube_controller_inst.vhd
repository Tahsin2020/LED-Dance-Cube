	component Cube_controller is
		port (
			clk_clk                          : in  std_logic                     := 'X';             -- clk
			reset_reset_n                    : in  std_logic                     := 'X';             -- reset_n
			uart_0_external_connection_rxd   : in  std_logic                     := 'X';             -- rxd
			uart_0_external_connection_txd   : out std_logic;                                        -- txd
			led_cube_uart_0_conduit_end_ledr : out std_logic_vector(7 downto 0);                     -- ledr
			led_cube_uart_0_conduit_end_sw   : in  std_logic_vector(9 downto 0)  := (others => 'X'); -- sw
			led_cube_uart_0_conduit_end_gpio : out std_logic_vector(35 downto 0)                     -- gpio
		);
	end component Cube_controller;

	u0 : component Cube_controller
		port map (
			clk_clk                          => CONNECTED_TO_clk_clk,                          --                         clk.clk
			reset_reset_n                    => CONNECTED_TO_reset_reset_n,                    --                       reset.reset_n
			uart_0_external_connection_rxd   => CONNECTED_TO_uart_0_external_connection_rxd,   --  uart_0_external_connection.rxd
			uart_0_external_connection_txd   => CONNECTED_TO_uart_0_external_connection_txd,   --                            .txd
			led_cube_uart_0_conduit_end_ledr => CONNECTED_TO_led_cube_uart_0_conduit_end_ledr, -- led_cube_uart_0_conduit_end.ledr
			led_cube_uart_0_conduit_end_sw   => CONNECTED_TO_led_cube_uart_0_conduit_end_sw,   --                            .sw
			led_cube_uart_0_conduit_end_gpio => CONNECTED_TO_led_cube_uart_0_conduit_end_gpio  --                            .gpio
		);

