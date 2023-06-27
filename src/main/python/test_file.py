### test file ####
import pickle
import numpy as np
import os

# user_dict = [['6kxHMpVt9OYDQOwXYJrQAb', 'listen', '4bKoCbveIKkAVdzj6h5jqX'], ['6kxHMpVt9OYDQOwXYJrQAb', 'listen', '2UFmTI7g4aLgpWmaCcTEvI'], ['6kxHMpVt9OYDQOwXYJrQAb', 'listen', '4M36kNFgfTRfssVd7EsAmA'], ['6kxHMpVt9OYDQOwXYJrQAb', 'listen', '4M36kNFgfTRfssVd7EsAmA'], ['6kxHMpVt9OYDQOwXYJrQAb', 'listen', '4M36kNFgfTRfssVd7EsAmA']]

# user_dict = [{'id': '6kxHMpVt9OYDQOwXYJrQAb', 'age': 17, 'listened_songs': ['4bKoCbveIKkAVdzj6h5jqX', '2UFmTI7g4aLgpWmaCcTEvI', '4M36kNFgfTRfssVd7EsAmA', '4M36kNFgfTRfssVd7EsAmA', '4M36kNFgfTRfssVd7EsAmA' , '4M36kNFgfTRfssVd7EsAmA', '4M36kNFgfTRfssVd7EsAmA' , '4M36kNFgfTRfssVd7EsAmA', '4M36kNFgfTRfssVd7EsAmA' , '4M36kNFgfTRfssVd7EsAmA', '4M36kNFgfTRfssVd7EsAmA']} , {'id': 'xxxxxxxxxxxxxxxxxxx', 'age': 17, 'listened_songs': []}]



# def check_condition(user):

#     # user_dict = user


#     with open(r'src\main\python\user_song_num.pickle', 'rb') as file:
#         data = pickle.load(file)

#     # go through all of the users that we have got from the receiver.user_to_dict() function
#     # for user in user_dict:

#     # store the each users id and listened songs and specify a boolean for if we find the user in the pickle data
#     user_id = user["id"]
#     listened_songs = user["listened_songs"]
#     user_found_in_pickle = False

#     # go through the dicts inside the data list and check all of the dictionaries
#     for i , user_song_num in enumerate(data):

#         # going through each dictionaries check if the id of the dict is the id of the user that we are checking for
#         if user_song_num["id"] == user_id :

#             # if you found a user in the one of the dicts store the song_nums of it and 
#             # specify that we have found the user in our pickle file
#             ls_num = user_song_num["song_num"]
#             user_found_in_pickle = True

#             # subtract the new listened songs from the song_nums of the pickle file 
#             # if the result is bigger that or equal to 4 return true meaning that we now
#             # want to give our user some recommendations
#             if (len(listened_songs) - ls_num) >= 4:

#                 data[i]["song_num"] = len(listened_songs)
#                 return True
            
#             else :
#                 return False
            
#     if not user_found_in_pickle :

#         user_song_num_dict = {}

#         user_song_num_dict["id"] = user_id.copy()
#         user_song_num_dict["song_num"] = 0

#         return False
                
            
# print(check_condition(user_dict))


# user_list = []


with open(r'src\main\python\user_song_num.pickle', 'rb') as file:
    data = pickle.load(file)
    print(data)


data[0]["song_num"] = 0


with open(r'src\main\python\user_song_num.pickle', 'wb') as file:
    pickle.dump(data , file)
    print(data)

# import soundfile as sf

# folder_path = r"C:\python\MACHINE LEARNING\recommendation_system\AP_project\front\new2_melody_hub\MelodyHub\src\main\resources\com\example\melodyhub\musics"

# def convert_to_wav():

#     for file_path in os.listdir(folder_path):
#         if file_path.endswith(".mp3"):

#             music_path = os.path.join(folder_path , file_path)

#             audio, sample_rate = sf.read(music_path)

#             file_name, file_extension = os.path.splitext(music_path)
#             wav_file = file_name + ".wav"

            

#             sf.write(wav_file, audio, sample_rate)


# convert_to_wav()




