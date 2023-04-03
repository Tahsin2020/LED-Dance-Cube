import os
import re
from rotating_wall_frames import create_rotating_wall_animation
from wave_animation import create_wave_animation
from cone_animation import create_cone_animation
from rolling_ball_animation import create_rolling_ball_animation
from rotating_diamond_animation import create_rotating_diamond_animation
from pulsating_wave_sphere import create_pulsating_wave_sphere_animation
from helix_animations import create_helix_animation

def write_animation_to_file(frames, filename):
    with open(filename, 'w') as f:
        for frame in frames:
            for i in range(8):
                for j in range(8):
                    hex_string = ''
                    byte = 0
                    for k in range(8):
                        if frame[k][j][7 - i] == 1:
                            byte += 1 << (7 - k)
                    hex_string = "{:02X}".format(byte)
                    f.write(hex_string + "\n")
cwd = os.getcwd()

write_animation_to_file(create_cone_animation(), cwd + "/hexes/hexecone.hex")
write_animation_to_file(create_helix_animation(), cwd + "/hexes/helix.hex")
write_animation_to_file(create_pulsating_wave_sphere_animation(), cwd + "/hexes/pulsating_sphere.hex")
write_animation_to_file(create_rolling_ball_animation(), cwd + "/hexes/rolling_ball.hex")
write_animation_to_file(create_rotating_diamond_animation(), cwd + "/hexes/diamond.hex")
write_animation_to_file(create_wave_animation(), cwd + "/hexes/waves.hex")
write_animation_to_file(create_rotating_wall_animation(), cwd + "/hexes/rotating_wall.hex")

# # Define the pattern to match function names
# pattern = r"^create_(.*)_animation$"

# # Get a list of all Python files in the current directory
# file_list = [f for f in os.listdir('.') if os.path.isfile(f) and f.endswith('.py')]

# print(os.getcwd())

# # Loop over each file in the list
# for filename in file_list:
#     # Open the file and read its contents
#     with open(filename) as f:
#         code = f.read()
#     # print("File {}: code={}".format(filename, code))
    
#     # Use regular expressions to find all function names that match the pattern
#     function_names = re.findall(pattern, code, re.MULTILINE)
#     print(function_names)
    
#     # Loop over each function name and call the corresponding function
#     for name in function_names:
#         # Import the module and get the function
#         module_name = filename[:-3]  # remove the .py extension
#         module = __import__(module_name)
#         func = getattr(module, "create_" + name + "_animation")
        
#         # Call the function to get the list of LED frames
#         frames = func()
        
#         # Write the frames to a hex file with the corresponding name
#         hex_filename = "hexes/" + name + "_animation.hex"
#         write_animation_to_file(frames, hex_filename)
