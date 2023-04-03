module LED_cube_stream_controller(
    input logic clk,
    input logic rst_n,
    input logic readdatavalid,
    input logic [7:0] data_in,
    input logic [3:0] mode,
    
    output logic stall_mode_change,
    output logic new_data,
    output logic [5:0] data_counter
);

    enum {WAIT, BEGIN_DATA_READ, DATA_READ, END_DATA_READ} state, next_state;

    // logic [5:0] data_counter;

    always_ff @( posedge clk ) begin : state_seq_logic_blk
        if( ~rst_n || mode != 4'h3) state <= WAIT;
        else state <= next_state;
    end

    always_comb begin : next_state_comb_logic_blk
        next_state = state;
        case(state)
            WAIT: if(mode == 4'h3) next_state = BEGIN_DATA_READ;
            BEGIN_DATA_READ: begin
                if(data_in == 8'h20) next_state = DATA_READ;
                else next_state = WAIT;
            end
            DATA_READ: if(data_counter == 7'd127) next_state = END_DATA_READ;
            END_DATA_READ: begin
                if(data_in == 8'h30) next_state = BEGIN_DATA_READ; 
                else next_state = WAIT;
            end
        endcase
    end

    assign stall_mode_change = (state == DATA_READ) ? 1'b1 : 1'b0;

    always_ff @( posedge clk ) begin : data_counter_blk
        if( ~rst_n || mode != 4'h3) data_counter <= 6'h0;
        else begin
            if(readdatavalid && state == DATA_READ) data_counter <= data_counter + 1'b1;
        end
    end

    

endmodule : LED_cube_stream_controller