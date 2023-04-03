module LED_cube_uart (
		output logic       avalon_master_read,          // avalon_master.read
		input  logic [15:0] avalon_master_readdata,      //              .readdata
		input  logic       avalon_master_readdatavalid, //              .readdatavalid
		input  logic       avalon_master_waitrequest,   //              .waitrequest
		output logic       avalon_master_write,         //              .write
		output logic [15:0] avalon_master_writedata,     //              .writedata
		output logic [4:0] avalon_master_address,       //              .address
		input  logic       clock_sink_clk,              //    clock_sink.clk
		input  logic       reset_sink_reset,            //    reset_sink.reset
		output logic [7:0] LEDR,                         //   conduit_end.new_signal
        input  logic [9:0] SW,
        output logic [35:0] GPIO_0
);

    logic clk, rst_n;

    assign clk = clock_sink_clk;
    assign rst_n = reset_sink_reset;

    logic [7:0] Layers_out, Latches_out, Data_out;
	assign {GPIO_0[32], GPIO_0[30], GPIO_0[28], GPIO_0[26], GPIO_0[35], GPIO_0[33], GPIO_0[31], GPIO_0[27]} = Layers_out;
	assign {GPIO_0[25], GPIO_0[7], GPIO_0[9], GPIO_0[13], GPIO_0[15], GPIO_0[19], GPIO_0[21], GPIO_0[23]} = Latches_out;
	assign {GPIO_0[2], GPIO_0[4], GPIO_0[6], GPIO_0[10], GPIO_0[12], GPIO_0[16], GPIO_0[18], GPIO_0[20]} = Data_out;

    logic [5:0] stream_data_counter;

    always_comb begin : LEDR_Debug_block
		case( SW[9:8] )
			2'b00: LEDR = uart_reg;
			2'b01: LEDR = {3'b0, avalon_master_address};
			2'b10: LEDR = {4'b0, mode};
			2'b11: LEDR = stream_data_counter;
		endcase
	end

// UART regs:
// 0 - rxdata
// 1 - txdata
// 2 - status
// 3 - control
// need 3 bits for address

// RXDATA:


// // STATUS REG:
// 5 - TMT => 0 - in the process of shifting out charater. 1 - idle (available)
// 6 - TRDY => 0 - register is full . 1 - register is empty and ready for a new character
// 7 - RRDY => 0 - register is empty and not ready to be read. 1 - register is ful and ready to be read

    enum {WAIT, READWAIT, READ} state, next_state; 

    logic [7:0] uart_reg;

    always_ff @(posedge clk) begin : ireg_logic_based_on 
        if( ~ rst_n ) state <= WAIT;
        else state <= next_state;
    end

    always_comb begin : next_state_logic
            next_state = WAIT;
            case(state)
                WAIT: if(avalon_master_readdata[7]) next_state = READ;
                READWAIT: if(avalon_master_readdata[7]) next_state = READ;
                READ:     next_state = READWAIT;
                default: next_state = WAIT;
            endcase
    end

    always_comb begin : avalon_slave_addr_logic 
        case(state)
            WAIT: avalon_master_address = 5'h08;
            READWAIT: avalon_master_address = 5'h08;
            READ: avalon_master_address = 0;
            default: avalon_master_address = 0;
        endcase
    end

    // assign avalon_master_write = (state == TRANSMIT) ? 1'b1 : 1'b0;

    assign avalon_master_read = (state == READ) ? 1'b1 : 1'b0;

    always_ff @(posedge clk) begin : ireg_logic
        if( ~rst_n ) uart_reg <= 0;
        else if(state == READ) uart_reg <= avalon_master_readdata[7:0];
    end

	assign avalon_master_writedata = {8'b0, uart_reg};

    logic [3:0] mode;

    LED_cube_driver driver(
        .clk(clk),
        .rst_n(rst_n),
        .uart_in(uart_reg),
        .readdatavalid(avalon_master_read),
        .mode(mode),

        .Layers_out(Layers_out), 
        .Latches_out(Latches_out), 
        .Data_out(Data_out),

        .stream_data_counter(stream_data_counter)
    );

endmodule : LED_cube_uart
