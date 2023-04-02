module LED_cube_stream(
    input logic clk,
    input logic rst_n,
    input logic [3:0] mode,
    input logic new_data,
    input logic [7:0] uart_reg,
    input logic [5:0] frame_addr,

    output logic [7:0] data_to_latch
);

    logic [5:0] addr_in;

    logic [2:0] offset_in;
    logic [2:0] offset_out;

    assign offset_out = offset_in - 1'b1;

    logic [7:0] stream_data1 [63:0];
	logic [7:0] stream_data2 [63:0];
	logic [7:0] stream_data3 [63:0];
	logic [7:0] stream_data4 [63:0];
	logic [7:0] stream_data5 [63:0];
	logic [7:0] stream_data6 [63:0];
	logic [7:0] stream_data7 [63:0];
	logic [7:0] stream_data8 [63:0];

    always_ff @( posedge clk ) begin : addr_in_logic
        if( !rst_n || mode != 4'h3 ) addr_in = 6'h0;
        else begin
            if(new_data) addr_in <= addr_in + 1'b1;
        end
    end

    always_ff @( posedge clk ) begin : offset_in_logic
        if( !rst_n || mode != 4'h3 ) offset_in = 3'h0;
        else begin
            if(new_data && addr_in == 6'b111111) offset_in <= offset_in + 1'b1;
        end
    end

    always_ff @( posedge clk ) begin : write_to_mem_logic
        if( ~rst_n ) begin
            for(int i = 0 ; i < 64 ; i ++) begin
                stream_data1[i] <= 8'h0;
                stream_data2[i] <= 8'h0;
                stream_data3[i] <= 8'h0;
                stream_data4[i] <= 8'h0;
                stream_data5[i] <= 8'h0;
                stream_data6[i] <= 8'h0;
                stream_data7[i] <= 8'h0;
                stream_data8[i] <= 8'h0;
            end
        end
        else begin
            if(new_data) begin
                case(offset_in)
                    3'b000: stream_data1[addr_in] <= uart_reg;
                    3'b001: stream_data2[addr_in] <= uart_reg;
                    3'b010: stream_data3[addr_in] <= uart_reg;
                    3'b011: stream_data4[addr_in] <= uart_reg;
                    3'b100: stream_data5[addr_in] <= uart_reg;
                    3'b101: stream_data6[addr_in] <= uart_reg;
                    3'b110: stream_data7[addr_in] <= uart_reg;
                endcase
            end
        end
    end

    always_comb begin : data_out_logic
        data_to_latch = 8'h0;
        case(offset_out)
            3'b000: data_to_latch = stream_data1[frame_addr]; 
            3'b001: data_to_latch = stream_data2[frame_addr]; 
            3'b010: data_to_latch = stream_data3[frame_addr]; 
            3'b011: data_to_latch = stream_data4[frame_addr]; 
            3'b100: data_to_latch = stream_data5[frame_addr]; 
            3'b101: data_to_latch = stream_data6[frame_addr]; 
            3'b110: data_to_latch = stream_data7[frame_addr]; 
        endcase
    end

endmodule : LED_cube_stream