val =0
squares = {1, 2, 16, 32, 1}

local adder = function()
    val=val+1
    if( val==6 )
    then
       val=1
    end
    uart.write(0,string.char(squares[val]))
end


mytimer = tmr.create()
mytimer:register(10000, tmr.ALARM_AUTO, adder)
mytimer:interval(5000) -- actually, 3 seconds is better!
mytimer:start()
