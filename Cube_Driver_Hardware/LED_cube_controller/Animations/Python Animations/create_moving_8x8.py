import numpy as np

def create_animation(input_plane, num_frames=150, move_every_n_frames=1):
    # Initialize an 8x8x8 cube with zeros
    cube = np.zeros((8, 8, 8), dtype=int)

    # Initialize the list of frames
    frames = []

    # Generate the frames of the animation
    for i in range(num_frames):
        # Clear the cube
        cube = np.zeros((8, 8, 8), dtype=int)

        # Determine the current z-coordinate based on the frame index and move_every_n_frames
        z_coord = ((7 - i) // move_every_n_frames) % 8

        # Set the input_plane to the appropriate position in the cube
        cube[z_coord] = input_plane
        cube[(z_coord + 1) % 8] = input_plane

        # Rotate the cube 90 degrees in the y-z plane
        cube_rotated = np.rot90(cube, axes=(0, 1))
        cube_rotated = np.rot90(cube_rotated, axes=(2, 0))
        # cube_rotated = cube

        # Append the current state of the cube to the frames list
        frames.append(np.copy(cube_rotated))

    return frames
