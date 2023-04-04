import numpy as np

def generate_vortex_frame(frame_number, num_frames):
    frame = np.zeros((8, 8, 8))
    
    # Parameters to control the vortex shape and speed
    base_radius = 0
    height_factor = 0.8
    radius_factor = 0.9
    slope = 2.5
    speed = ( 5 * np.pi * np.sin( (frame_number/num_frames) * 2 * np.pi )) / num_frames
    
    for y in range(8):
        for angle_step in np.linspace(0, 2 * np.pi, 12):
            # Calculate the x and z coordinates for the vortex
            angle = angle_step + speed * frame_number
            
            # Calculate the radius based on the height (y)
            radius = base_radius + radius_factor * y
            x = int(radius * np.cos(angle) + 3.5)
            z = int(radius * np.sin(angle) + 3.5)
            
            # Calculate the adjusted_y using the line equation with a constant slope
            adjusted_y = int(y + slope * angle_step)
            
            if 0 <= x < 8 and 0 <= adjusted_y < 8 and 0 <= z < 8:
                frame[x, z, adjusted_y] = 1
                
    return frame



def create_cone_animation():
    num_frames = 150
    vortex_frames = [generate_vortex_frame(i, num_frames) for i in range(num_frames)]
    return vortex_frames