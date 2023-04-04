import numpy as np

def generate_wave_frame(frame_number, num_frames):
    frame = np.zeros((8, 8, 8))
    
    # Parameters to control the wave shape and speed
    frequency = 1
    amplitude = frame_number / (num_frames * 3 / 4) + 1
    speed = 16 * np.pi / num_frames

    height = 0;
    # if(0 >= frame_number < 30):
    #     height = 0;
    # elif(30 <= frame_number < 60):
    #     height = 1;    
    # elif (60 <= frame_number < 90):
    #     height = 0;
    # elif (90 <= frame_number < 120):
    #     height = -1
    # elif (120 <= frame_number <= 150):
    #     height = 0;
    
    for x in range(8):
        for y in range(8):
            # Calculate the y-coordinate for the wave
            z = int(amplitude * np.sin(frequency * (x + speed * frame_number)) + amplitude * np.sin(frequency * (y + speed * frame_number))) + 4 + height
            
            if 0 <= z < 8:
                frame[x, y, z] = 1

                
    return frame

def create_wave_animation():
    num_frames = 150
    wave_frames = [generate_wave_frame(i, num_frames) for i in range(num_frames)]
    return wave_frames