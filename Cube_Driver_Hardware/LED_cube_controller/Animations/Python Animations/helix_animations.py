import numpy as np
import math

num_frames = 150
num_rotations = 3

def is_close_to_helix(x, y, z, t, helix_offset):
    theta = np.arctan2(y - 3.5, x - 3.5) + helix_offset + 2 * math.pi * num_rotations * t
    ideal_z = 3.5 + 3.5 * math.sin(theta)
    return abs(z - ideal_z) < 0.6

def generate_helix_frame(t):
    frame = np.zeros((8, 8, 8))
    for x in range(8):
        for y in range(8):
            for z in range(8):
                helix_1 = is_close_to_helix(x, y, z, t, 0)
                helix_2 = is_close_to_helix(x, y, z, t, 2 * math.pi / 3)
                helix_3 = is_close_to_helix(x, y, z, t, 4 * math.pi / 3)

                if helix_1 or helix_2 or helix_3:
                    frame[x, y, z] = 1
    return frame

def create_helix_animation():
    frames = []
    for frame in range(num_frames):
        t = frame / num_frames
        frames.append(generate_helix_frame(t))
    return frames
