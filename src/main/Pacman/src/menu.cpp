#include <bits/stdc++.h>
#include <unistd.h>


#include "print.cpp"



using namespace std;

void showMenu(string& username,int& level, int& width , int& height ,int& finalRecord);
void printpacman();
void ranks();
void load(string username , int & level , int & width , int& height , int& finalRecord);

void load(string username , int & level , int & width , int& height , int& finalRecord)
{
	#ifdef _WIN32
		system("cls");
	#else
		system("clear");
	#endif
	string line;
	ifstream file(username+".pacman");
	if(file.is_open())
	{
		string finalRecordString;
		string levelString;
		string widthString;
		string heightString;
		file>>finalRecordString>>levelString>>widthString>>heightString;

		finalRecord=stoi(finalRecordString);
		level = stoi(levelString);
		width = stoi(widthString);
		height = stoi(heightString);
		return ;
	}else
	{
		cout<<"sorry something went wrong so your load failed :(((\n a new game wiht your username is opening.\n";
		finalRecord=0;
		level = 0;
		width = 25;
		height = 25;
		return ;
	}
}
void ranks()
{
	pair<int,string>* ranks = new pair<int,string> [5];
	int size=0,capacity=5;
	#ifdef _WIN32
		system("cls");
	#else
		system("clear");
	#endif
	string line;
	ifstream file("ranks.pacman");
	if(file.is_open())
	{
		string player,name;
		while(getline(file,player)) {
			if(size==capacity)
			{
				pair<int,string>* ranks2 = new pair<int,string> [capacity*2];
				for( int j = 0 ; j< size ; j++)
				{
					ranks2[j]=ranks[j];
				}
				ranks=ranks2;
				delete[] ranks2;
			}
			
			for(int i = 0; true ; i++)
			{
				if(player[i]==':')
				{
					name=player.substr(0,i);
					ranks[size]=make_pair(stoi(player.substr(i+1)),name);
					break;
				}
			}
			size++;
		}

		if(size == 0) {
			cout << "No records found."<<endl;
		} else {
			sort(ranks, ranks+size, greater<>());
			for ( int i = 0 ; i < size ; i++ )
			{
				cout<<ranks[i].second<<": "<<ranks[i].first<<endl;
			}
		}
		delete [] ranks;
	}
	else
	{
		cout<<"sorry something went wrong so your load failed :(((\n";
	}
	cout << "press a key to exit." << endl;
	getline(cin,line);

	return;
}
void printpacman()
{
	cout<<"       *********           **              *********             **            **                    **              **      **    **  **\n";
	cout<<"       **     **         **  **            **                  **  **        **  **                **  **            ****    **    **  **\n";
	cout<<"       **     **        **    **           **                 **    **      **     **             **    **           ** **   **    **  **\n";
	cout<<"       **     **       **********          **                **      **    **       **           **********          **  **  **    **  **\n";
	cout<<"       *********      **        **         **               **        **  **         **         **        **         **   ** **          \n";
	cout<<"       **            **          **        **              **          ****           **       **          **        **    ****    **  **\n";
	cout<<"       **           **            **       **********     **            **             **     **            **       **      **    **  **\n";

}
void showMenu(string& username,int& level, int& width , int& height ,int& finalRecord)
{
	
	string input = "init";
	int index=1;
	int * size=new int [6];
	size[1]=1;
	size[2]=1;
	size[3]=1;
	size[4]=1;
	size[5]=1;
	
	do
	{
		
		if(input!="w" && input!="s" && input!="init")
		{

		}else if(input=="w" && index>1)
		{
			index--;
		}else if(input=="w" && index==1)
		{
			index=5;
		}else if(input=="s" && index<5)
		{
			index++;
		}else if(input=="s" && index==5)
		{
			index=1;
		}
		#ifdef _WIN32
			system("cls");
		#else
			system("clear");
		#endif
		printpacman();
		size[index]=2;
		cout << endl;
		sevenSegment("new game" , size[1]);
		cout << endl;
		sevenSegment("load" , size[2]);
		cout << endl;
		sevenSegment("ranks" , size[3]);
		cout << endl;
		sevenSegment("guide", size[4]);
		cout<<endl;
		sevenSegment("exit" , size[5]);
		size[index]=1;
		if((getline(cin, input) && input.length() > 0)) {
			if(input != "s" && input != "w") {
				continue;
			}
		} else {
			break;
		}
	} while(true);
	
	input = to_string(index);

	if(input=="1")
	{
		cout<<"enter your name:\n";
		cin>>username;
		cout << "enter desired width & height:\n";
		cin>>width >> height;
		return;
	}else if(input =="2")
	{
		cout<<"enter your name:\n";
		cin>>username;
		load(username ,level , width , height , finalRecord);
		return;
	}else if(input=="3")
	{
		ranks();
		showMenu( username, level, width , height , finalRecord);
	}else if(input == "4")
	{
		#ifdef _WIN32
			system("cls");
		#else
			system("clear");
		#endif
		string guide="use 'w' to up, 'a' to left, 's' to down,'d' to right.\ntrun only on crossroad.\npause game by press 'p'.\n";
		guide+="you can save game only on checkpoints.\nuse a special username to don't have anyproblem later.\nyou can eliminate ghosts just on super power mode, in this mode your character changes from 'p' to 'S', otherwise the ghosts kill you when you hit them and your hp decrease .\n";
		guide+="and exit the game by press 'e'.\n";
		printString(guide);
		showMenu( username, level, width , height , finalRecord);
	}else if(input == "5")
	{
		exit(0);
	}else
	{
		#ifdef _WIN32
			system("cls");
		#else
			system("clear");
		#endif
		cout<<"your input isn't valid.\n";
		sleep(1);

		showMenu(username,level , width,height , finalRecord);
	} 
}