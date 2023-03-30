module Config_Space(
    input logic clk,
    input logic rst_n,
    input logic [7:0] uart_in,
    output logic [3:0] brightness,
    output logic [3:0] mode,
    output logic [3:0] animation_sel
);

    logic [3:0] conf_i, conf_d;
    assign conf_i = uart_in[7:4];
    assign conf_d = uart_in[3:0];


    // MODE:
    // 0x00 - OFF
    // 0x01 - ANIM_LOOP
    // 0x02 - ANIM_SEL
    // 0x03 - STREAM
    // 0x04 - PLANE_MSG
    // 0x05 - ALL_ON
    // 0x0F - ANIM_DB
    always_ff @(posedge clk) begin : mode_register
        if( ~rst_n ) mode <= 0;
        else if(conf_i == 4'd0) mode <= conf_d;
    end
    
    always_ff @(posedge clk) begin : brightness_register
        if( ~rst_n ) brightness <= 4'hf;
        else if(conf_i == 4'd1) brightness <= conf_d;
    end


    
    always_ff @(posedge clk) begin : animation_sel_register
        if( ~rst_n ) animation_sel <= 0;
        else if(mode == 4'h2) animation_sel <= conf_i[2:0];
    end

endmodule : Config_Space