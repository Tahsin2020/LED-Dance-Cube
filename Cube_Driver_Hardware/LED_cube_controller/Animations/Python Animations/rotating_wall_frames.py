import numpy as np
import math

# Define the number of rotations during the animation
num_rotations = 3
# Define the width and height of the wall
wall_width = 1
wall_height = 8
# Define the starting position of the wall
start_x = 2
start_y = 0

def generate_rotating_wall_frame(angle):
    frame = np.zeros((8, 8, 8))
    for x in range(8):
        for y in range(8):
            # Rotate the pixel position around the center of the display
            x_rot = (x - 3.5) * math.cos(angle) - (y - 3.5) * math.sin(angle) + 3.5
            y_rot = (x - 3.5) * math.sin(angle) + (y - 3.5) * math.cos(angle) + 3.5

            # Check if the pixel is within the wall area
            if start_x <= x_rot < start_x + wall_width and start_y <= y_rot < start_y + wall_height:
                frame[x, 0:8, y] = 1  # Turn on the pixel if it's within the wall area
            else:
                frame[x, 0:8, y] = 0  # Turn off the pixel if it's outside the wall area
    return frame

def create_rotating_wall_animation():
    # Generate a list of 150 frames of 8x8x8 arrays
    frames = []
    for frame in range(150):
        # Calculate the rotation angle for this frame
        angle = frame / 150 * num_rotations * 2 * math.pi
        frames.append(generate_rotating_wall_frame(angle))
    return frames