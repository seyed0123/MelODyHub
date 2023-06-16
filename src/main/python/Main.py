### main file
import Receiver as receiver
import Recommender as recommender
import Database_Setter as setter
from pprint import pprint



if __name__ == '__main__':
    
    # receiver.connect_to_database()

    # the main loop
    while True:
        condition = receiver.check_condition() # if condition is satisfied

        if condition :
            table = receiver.get_table() # get tables from database
            user_dict = receiver.user_to_dict(table) # make a list of dictionaries containing each users history

            # loop through each user and recommend songs 
            for user in user_dict :
                user , recoms = recommender.recommend(user)
                # print(len(recoms))

                for recom in recoms :
                    
                    print(recom)
        break