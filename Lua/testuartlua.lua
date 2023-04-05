val =0
--squares = {1, 2, 16, 32, 1}

local adder = function()
    val=val+1
    if( val==3 )
    then
       val=1
    end
    if(val ==1) then
        uart.write(0,string.char(3))
    elseif (val ==2) then
        uart.write(0,string.char(32))
        for i = 1, 64 do
            if (i~=3)then
                uart.write(0,string.char(i)) 
            end
        end
        uart.write(0,string.char(48))
    end
--    uart.write(0,string.char(squares[val]))
end


mytimer = tmr.create()
mytimer:register(10000, tmr.ALARM_AUTO, adder)
mytimer:interval(5000) -- actually, 3 seconds is better!
mytimer:start()
