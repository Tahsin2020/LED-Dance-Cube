import numpy as np
import math

num_frames = 150
num_rotations = 3

def diamond_shape(x, y, z):
    return (abs(x - 3.5) + abs(y - 3.5) + abs(z - 3.5)) <= 3.5

def generate_rotating_diamond_frame(angle):
    frame = np.zeros((8, 8, 8))
    for x in range(8):
        for y in range(8):
            for z in range(8):
                x_rot = (x - 3.5) * math.cos(angle) - (y - 3.5) * math.sin(angle) + 3.5
                y_rot = (x - 3.5) * math.sin(angle) + (y - 3.5) * math.cos(angle) + 3.5

                if diamond_shape(x_rot, y_rot, z):
                    frame[x, y, z] = 1
    return frame

def create_rotating_diamond_animation():
    frames = []
    for frame in range(num_frames):
        angle = frame / num_frames * num_rotations * 2 * math.pi
        frames.append(generate_rotating_diamond_frame(angle))
    return frames