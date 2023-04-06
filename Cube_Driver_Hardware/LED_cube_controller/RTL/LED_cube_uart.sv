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
        output logic [7:0] Layers_out,
        output logic [7:0] Latches_out,
        output logic [7:0] Data_out,
        output logic [6:0] HEX0,
        output logic [6:0] HEX1
);

    logic clk, rst_n;

    assign clk = clock_sink_clk;
    assign rst_n = reset_sink_reset;


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

    enum bit [1:0] {WAIT, READ} state, next_state; 

    logic [7:0] uart_reg;

    always_ff @(posedge clk) begin : ireg_logic_based_on 
        if( ~ rst_n ) state <= WAIT;
        else state <= next_state;
    end

    always_comb begin : next_state_logic
            next_state = WAIT;
            case(state)
                WAIT: if(avalon_master_readdata[7]) next_state = READ;
                READ: begin
                    if(read_counter == 2'd3) next_state = WAIT;
                    else next_state = READ;
                end
                default: next_state = WAIT;
            endcase
    end

    logic [1:0] read_counter;

    always_ff @( posedge clk ) begin : read_counter_clk
        if( ~rst_n || state != READ ) read_counter <= 0;
        else read_counter = read_counter + 1'b1;
    end

    always_comb begin : avalon_slave_addr_logic 
        case(state)
            WAIT: begin
                if(~avalon_master_readdata[7]) avalon_master_address = 5'h08;
                else avalon_master_address = 0;
            end
            READ: begin
                if(read_counter == 2'b0) avalon_master_address = 0;
                else avalon_master_address = 5'h08;
            end
            default: avalon_master_address = 0;
        endcase
    end

    // assign avalon_master_write = (state == TRANSMIT) ? 1'b1 : 1'b0;

    assign avalon_master_read = (state == READ) ? 1'b1 : 1'b0;

    always_ff @(posedge clk) begin : ireg_logic
        if( ~rst_n ) uart_reg <= 0;
        else if(read_counter == 2'd2) uart_reg <= avalon_master_readdata[7:0];
    end

	assign avalon_master_writedata = {8'b0, uart_reg};

    logic [3:0] mode;

    LED_cube_driver driver(
        .clk(clk),
        .rst_n(rst_n),
        .uart_in(uart_reg),
        .readdatavalid(readdatavalid),
        .mode(mode),

        .Layers_out(Layers_out), 
        .Latches_out(Latches_out), 
        .Data_out(Data_out)
    );

    logic [5:0] data_counter;

    always_ff @( posedge clk ) begin
        if( ~rst_n ) data_counter <= '0;
        else begin 
            if(state != READ && avalon_master_readdata[7] == 1'b1) 
                data_counter <= data_counter + 1'b1;
        end
    end

    logic readdatavalid;
    assign readdatavalid = (read_counter == 2'd1) ? 1'b1 : 1'b0;


    logic [3:0] out1, out2;

   BCD bcd_converted(
        .in(data_counter),
        .out1(out1),
        .out2(out2)
   );

   HEX hex0(
      .hex_in(out1),
      .hex_out(HEX0)
   );

    HEX hex1(
      .hex_in(out2),
      .hex_out(HEX1)
   );

endmodule : LED_cube_uart
