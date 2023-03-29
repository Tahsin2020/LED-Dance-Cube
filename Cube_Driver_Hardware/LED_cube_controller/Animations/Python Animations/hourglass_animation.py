import numpy as np

def generate_hourglass_frame(frame_number, num_frames):
    frame = np.zeros((8, 8, 8))

    # Draw the hourglass shape
    for y in range(8):
        radius = 3 - abs(y - 3.5)
        for x in range(8):
            for z in range(8):
                if abs(x - 3.5) <= radius and abs(z - 3.5) <= radius:
                    frame[x, y, z] = 1

    # Calculate the amount of sand that has fallen
    sand_fallen = int(4 * frame_number / num_frames)

    # Remove sand from the top half of the hourglass
    for y in range(4 - sand_fallen, 4):
        for x in range(8):
            for z in range(8):
                if abs(x - 3.5) <= (y - 0.5) and abs(z - 3.5) <= (y - 0.5):
                    frame[x, y, z] = 0

    # Add sand to the bottom half of the hourglass
    for y in range(3, 3 - sand_fallen, -1):
        for x in range(8):
            for z in range(8):
                if abs(x - 3.5) <= (3.5 - y) and abs(z - 3.5) <= (3.5 - y):
                    frame[x, y, z] = 1

    return frame

def create_hourglass_animation():
    num_frames = 150
    hourglass_frames = [generate_hourglass_frame(i, num_frames) for i in range(num_frames)]
    return hourglass_frames