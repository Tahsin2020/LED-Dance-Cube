module LED_cube_uart (
		output wire       avalon_master_read,          // avalon_master.read
		input  wire [15:0] avalon_master_readdata,      //              .readdata
		input  wire       avalon_master_readdatavalid, //              .readdatavalid
		input  wire       avalon_master_waitrequest,   //              .waitrequest
		output wire       avalon_master_write,         //              .write
		output wire [15:0] avalon_master_writedata,     //              .writedata
		output wire [4:0] avalon_master_address,       //              .address
		input  wire       clock_sink_clk,              //    clock_sink.clk
		input  wire       reset_sink_reset,            //    reset_sink.reset
		output wire [7:0] LEDR,                         //   conduit_end.new_signal
        input logic [9:0] SW,
        output logic [35:0] GPIO_0
);

    logic clk, rst_n;

    assign clk = clock_sink_clk;
    assign rst_n = reset_sink_reset;

    logic [7:0] Layers_out, Latches_out, Data_out;
	assign {GPIO_0[32], GPIO_0[30], GPIO_0[28], GPIO_0[26], GPIO_0[35], GPIO_0[33], GPIO_0[31], GPIO_0[27]} = Layers_out;
	assign {GPIO_0[25], GPIO_0[7], GPIO_0[9], GPIO_0[13], GPIO_0[15], GPIO_0[19], GPIO_0[21], GPIO_0[23]} = Latches_out;
	assign {GPIO_0[2], GPIO_0[4], GPIO_0[6], GPIO_0[10], GPIO_0[12], GPIO_0[16], GPIO_0[18], GPIO_0[20]} = Data_out;

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
                WAIT: if(avalon_master_readdatavalid) next_state = READ;
                READWAIT: if(avalon_master_readdatavalid) next_state = READ;
                READ:     next_state = READWAIT;
                default: next_state = WAIT;
            endcase
    end

    always_comb begin : avalon_slave_addr_logic 
        case(state)
            WAIT: avalon_master_address = 0;
            READWAIT: avalon_master_address = 0;
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

    LED_cube_driver driver(
        .clk(clk),
        .rst_n(rst_n),
        .uart_in(uart_reg),
        .SW(SW),
        .LEDR(LEDR),
        .uart_reg(uart_reg),

        .Layers_out(Layers_out), 
        .Latches_out(Latches_out), 
        .Data_out(Data_out)
    );

endmodule : LED_cube_uart
