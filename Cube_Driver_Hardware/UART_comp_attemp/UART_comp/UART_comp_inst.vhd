	component UART_comp is
		port (
			clk_clk       : in  std_logic := 'X'; -- clk
			gpio_035_rxd  : in  std_logic := 'X'; -- rxd
			gpio_035_txd  : out std_logic;        -- txd
			reset_reset_n : in  std_logic := 'X'  -- reset_n
		);
	end component UART_comp;

	u0 : component UART_comp
		port map (
			clk_clk       => CONNECTED_TO_clk_clk,       --      clk.clk
			gpio_035_rxd  => CONNECTED_TO_gpio_035_rxd,  -- gpio_035.rxd
			gpio_035_txd  => CONNECTED_TO_gpio_035_txd,  --         .txd
			reset_reset_n => CONNECTED_TO_reset_reset_n  --    reset.reset_n
		);

