module Config_Space(
    input logic clk,
    input logic rst_n,
    input logic [7:0] uart_in,
    input logic stall_mode_change,
    output logic [3:0] brightness,
    output logic [3:0] mode,
    output logic [3:0] animation_sel
);

    logic [3:0] conf_i, conf_d;
    assign conf_i = uart_in[7:4];
    assign conf_d = uart_in[3:0];

    always_ff @(posedge clk) begin : mode_register
        if( ~rst_n ) mode <= 0;
        else if(conf_i == 4'd0 && ~stall_mode_change) mode <= conf_d;
    end
    
    always_ff @(posedge clk) begin : brightness_register
        if( ~rst_n ) brightness <= 4'hf;
        else if(conf_i == 4'ha && mode != 4'h3) brightness <= conf_d;
    end
    
    always_ff @(posedge clk) begin : animation_sel_register
        if( ~rst_n ) animation_sel <= 4'b1;
        else if(mode == 4'd2) animation_sel <= conf_i;
    end

endmodule : Config_Space