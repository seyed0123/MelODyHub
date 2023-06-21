### main file
import Receiver as receiver
import Recommender as recommender
import Database_Pusher as DP
import time


if __name__ == '__main__':
    
    # the main loop
    while True:
        table = receiver.get_table() # get table from database
        user_dict = receiver.user_to_dict(table) # make a list of dictionaries containing each users history

        # loop through each user and recommend songs 
        for user in user_dict :
            condition = receiver.check_condition(user) # if condition is satisfied

            if condition :
                user_id , recoms = recommender.recommend(user)
                DP.push_to_database(user_id , recoms)



        time.sleep(3)
        break