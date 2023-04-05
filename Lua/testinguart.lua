val =0

local adder = function()
    uart.write(0,string.char(val))
    if( val==1 )
    then
        val=0
    else
       val=1
    end
end


mytimer = tmr.create()
mytimer:register(10000, tmr.ALARM_AUTO, adder)
mytimer:interval(5000) -- actually, 3 seconds is better!
mytimer:start()
