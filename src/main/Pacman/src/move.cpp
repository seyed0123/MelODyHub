// standard library
#include <bits/stdc++.h>
#include <unistd.h>
#include <fstream>

// Adding terminal interactivity depending on the OS (Mac currently not supported)
#ifdef _WIN32
#include <conio.h>
#include <cstdlib>
#include <conio.h>
#include <windows.h>
#endif

#ifdef linux
#include <unistd.h>
#include <sys/ioctl.h>
#include <termios.h>

char getch() {
        char buf = 0;
        struct termios old = {0};
        if (tcgetattr(0, &old) < 0)
                perror("tcsetattr()");
        old.c_lflag &= ~ICANON;
        old.c_lflag &= ~ECHO;
        old.c_cc[VMIN] = 1;
        old.c_cc[VTIME] = 0;
        if (tcsetattr(0, TCSANOW, &old) < 0)
                perror("tcsetattr ICANON");
        if (read(0, &buf, 1) < 0)
                perror ("read()");
        old.c_lflag |= ICANON;
        old.c_lflag |= ECHO;
        if (tcsetattr(0, TCSADRAIN, &old) < 0)
                perror ("tcsetattr ~ICANON");
        return (buf);
}

int kbhit()
{
    termios term;
    tcgetattr(0, &term);

    termios term2 = term;
    term2.c_lflag &= ~ICANON;
    tcsetattr(0, TCSANOW, &term2);

    int byteswaiting;
    ioctl(0, FIONREAD, &byteswaiting);

    tcsetattr(0, TCSANOW, &term);

    return byteswaiting;
}
#endif

#include "vector.cpp"
#include "maze.cpp"
#include "menu.cpp"

using namespace std;
Maze maze;


struct point
{
	int x,y;
};

point input();
void pacmanMove(point &pacman , point &direction , int &superPower , int &record , point before );
void ghostMove(point &ghost , int superPower, point &pacman , point ghostArr[] , int &hp , int &record , int& , point , point , point* ,int *);
void ghostINTELEGENCEmove(point &ghost , int superPower, point &pacman , point ghostArr[] , int &hp , int &record , int& , point , point , point & , point*  , int *);
bool move(int length,int  height ,point  pacmanRespawnPoint ,point ghostRespawnPoint ,int  level ,int& finalRecord);
void ghostRespawn(point &ghost , point ghostRespawnPoint , int &numJail ,int & record , point&, int* , int );
void pacmanRespawn(point &pacman , point pacmanRespawnPoint , int &hp ,point ghost[] ,int & numJail , point ghostRespawnPoint , point* , int*);
string chooseWallUnicode(int row, int col);
bool print( int length,int height, point pacman , point ghost[], int superPower , int record , int  hp);
bool checkContradiction(point &ghost , point &pacman , int superPower , int &hp , int &record ,point pacmanRespawnPoint , point ghostRespawnPoint , point& , point* , int*);
void save(string  username , int level , int width , int height ,int finalRecord);


void save(string username , int level , int width , int height, int finalRecord)
{
	ofstream saveFile;
	username+=".pacman";
	saveFile.open(username);
	if(saveFile.is_open())
	{
		saveFile<<finalRecord<<endl<<level<<endl<<width<<endl<<height<<endl;
		saveFile.close();
	}else
	{
		cout<<"something went wrong.\n";
	}
	sleep(1);
}
void pacmanRespawn(point &pacman , point pacmanRespawnPoint , int &hp ,point ghost[] ,int & numJail , point ghostRespawnPoint  , point ghostBeforeArr[] , int jail [])
{
	pacman=pacmanRespawnPoint;
	hp--;
	numJail=0;
	ghostBeforeArr[1]={0,-1};
	ghostBeforeArr[2]={0,1};
	ghostBeforeArr[3]={0,-1};
	ghostBeforeArr[4]={0,1};
	
	jail[1]=1;
	jail[2]=2;
	jail[3]=3;
	jail[4]=4;

	ghost[1].x=ghostRespawnPoint.x;
	ghost[1].y=ghostRespawnPoint.y;
	ghost[2].x=ghostRespawnPoint.x+1;
	ghost[2].y=ghostRespawnPoint.y;
	ghost[3].x=ghostRespawnPoint.x;
	ghost[3].y=ghostRespawnPoint.y+1;
	ghost[4].x=ghostRespawnPoint.x+1;
	ghost[4].y=ghostRespawnPoint.y+1;
	#ifdef _WIN32
		system("cls");
	#else
		system("clear");
	#endif
	cout<<"you are busted \n";
	sleep(2);
	return;
}
void ghostRespawn(point &ghost , point ghostRespawnPoint , int &numJail ,int & record , point &ghostBefore , int jail[] , int index)
{
	ghost=ghostRespawnPoint;
	ghostBefore.x=0;
	ghostBefore.y=-1;
	for(int i = 1 ; i<= 4 ; i++ )
	{
		if(jail[i]==0)
		{
			jail[i]=index;
			break;
		}
	}
	numJail++;
	record+=200;
	cout<<"+200 point\n";
	sleep(2);
	return;
}
bool checkContradiction(point &ghost ,point ghostArr[] , point &pacman , int superPower , int &hp , int &record ,int & numJail,point pacmanRespawnPoint , point ghostRespawnPoint , point &ghostBefore ,point ghostBeforeArr[] ,int  jail[] )
{
	if((ghost.x==pacman.x) && (ghost.y==pacman.y))
	{
		if(superPower>0)
		{
			int index=0;
			for(int i = 1; i <= 4; i++) 
			{
				if((ghost.x==ghostArr[i].x) && (ghost.y==ghostArr[i].y)) {
					index = i;
					break;
				}
			}
			if((ghostBefore.x==ghostBeforeArr[0].x)&&(ghostBefore.y==ghostBeforeArr[0].y))
			{
				ghostRespawn( ghost , ghostRespawnPoint , numJail , record , ghostBeforeArr[index] , jail , index);
			} else 
			{
				ghostRespawn( ghost , ghostRespawnPoint , numJail , record , ghostBefore , jail , index );
			}
		}else
		{
			pacmanRespawn( pacman ,  pacmanRespawnPoint ,  hp , ghostArr , numJail , ghostRespawnPoint , ghostBeforeArr , jail );
		}
		return 1;
	}
	return 0;
}
void superGhost(point &ghost , int superPower, point &pacman , point ghostArr[] , int &hp , int &record , int &numJail , point pacmanRespawnPoint , point ghostRespawnPoint , point &ghostBefore  , point ghostBeforeArr[] , int jail[])
{
	if(checkContradiction(ghost , ghostArr, pacman , superPower , hp , record ,numJail, pacmanRespawnPoint , ghostRespawnPoint , ghostBefore , ghostBeforeArr , jail ))
	{
		return;
	}	
	point direction={0,0};
	if(pacman.x>ghost.x)
	{
		direction.x=1;
	}else if( pacman.x == ghost.x )
	{
		direction.x=0;
	}else 
	{
		direction.x=-1;
	}
	
	if(pacman.y>ghost.y)
	{
		direction.y=1;
	}else if( pacman.y == ghost.y)
	{
		direction.y=0;
	}else 
	{
		direction.y=-1;
	}
	ghost.x+=direction.x;
	ghost.y+=direction.y;
	if(checkContradiction(ghost , ghostArr, pacman , superPower , hp , record ,numJail, pacmanRespawnPoint , ghostRespawnPoint , ghostBefore , ghostBeforeArr , jail))
	{
		return;
	} 
	{
		for(int i = 1 ,count = 0; i <= 4 ;i++)
		{
			if((ghost.x==ghostArr[i].x) && (ghost.y==ghostArr[i].y))
			{
				count++;
			}
			if(count==2)
			{
				ghost.x-=direction.x;
				ghost.y-=direction.y;
				direction.x*=-1;
				direction.y*=-1;
				ghost.x+=direction.x;
				ghost.y+=direction.y;
				break;
			}
		}
	}
	return;
}
void ghostINTELEGENCEmove(point &ghost , int superPower, point &pacman , point ghostArr[]  , int &hp , int &record , int &numJail , point pacmanRespawnPoint ,point ghostRespawnPoint , point &ghostBefore , point ghostBeforeArr[] , int jail[])
{
	if(checkContradiction(ghost , ghostArr, pacman , superPower , hp , record ,numJail, pacmanRespawnPoint , ghostRespawnPoint , ghostBefore , ghostBeforeArr , jail ))
	{
		return;
	}
	
	point* aim = new point [5];
	point direction=ghostBefore;
	int count=0 , defally = 0 , defallx = 0;
	if(maze[ghost.x+1][ghost.y]==2 ||maze[ghost.x+1][ghost.y]==3 ||maze[ghost.x+1][ghost.y]==0 )
	{
		count++;
		aim[count]={1,0};
		defallx=1;
	}
	if(maze[ghost.x][ghost.y+1]==2 ||maze[ghost.x][ghost.y+1]==3 || maze[ghost.x][ghost.y+1]==0 )
	{
		count++;
		aim[count]={0,1};
		defally =1;
	}
	if(maze[ghost.x-1][ghost.y]==2 ||maze[ghost.x-1][ghost.y]==3 || maze[ghost.x-1][ghost.y]==0 )
	{
		count++;
		aim[count]={-1,0};
		defallx=1;
	}
	if(maze[ghost.x][ghost.y-1]==2 ||maze[ghost.x][ghost.y-1]==3 || maze[ghost.x][ghost.y-1]==0 )
	{
		count++;
		aim[count]={0,-1};
		defally=1;
	}

	if(count>2 || (count==2 && defallx==1 && defally == 1))
	{
		int numOfMin = 0;
		int* arr = new int [5];
		pair<int,int> minimum ={pacman.x-ghost.x,pacman.y-ghost.y};
		for(int i = 1 ; i <= count ; i++)
		{
			if(aim[i].x*minimum.first > 0 || aim[i].y * minimum.second > 0 )
			{
				numOfMin++;
				arr[numOfMin]=i;
			}
		}
		if(numOfMin != 0) {
			int ra=(rand()%numOfMin) +1;
			direction=aim[arr[ra]];
		} else {
			int ra=(rand()% count)+1;
			direction=aim[ra];
		}

		delete[] arr;
	}
	
	if(superPower>0)
	{
		direction.x*=-1;
		direction.y*=-1;
	}
	
	ghostBefore=direction;
	ghost.x+=direction.x;
	ghost.y+=direction.y;
	
	if(checkContradiction(ghost , ghostArr, pacman , superPower , hp , record , numJail , pacmanRespawnPoint , ghostRespawnPoint , ghostBefore , ghostBeforeArr , jail ))
	{
		return;
	}
		
	for(int i = 1 , counter = 0; i <= 4 ;i++)
	{	
		
		if((ghost.x==ghostArr[i].x) && (ghost.y==ghostArr[i].y))
		{
			counter++;
		}
		if(counter==2)
		{
			ghost.x-=direction.x;
			ghost.y-=direction.y;
			direction.x*=-1;
			direction.y*=-1;
			ghost.x+=direction.x;
			ghost.y+=direction.y;
			
			break;
		}	
	}
	if(maze[ghost.x][ghost.y]==1 || maze[ghost.x][ghost.y]==-1)
	{
		ghost.x-=direction.x;
		ghost.y-=direction.y;
	}
	ghostBefore=direction;
	delete[] aim;
	return;
}
void ghostMove(point &ghost , int superPower, point &pacman , point ghostArr[] , int &hp , int &record , int &numJail , point pacmanRespawnPoint , point ghostRespawnPoint , point &ghostBefore  , point ghostBeforeArr[] , int jail[])
{
	if(checkContradiction(ghost , ghostArr, pacman , superPower , hp , record ,numJail, pacmanRespawnPoint , ghostRespawnPoint , ghostBefore , ghostBeforeArr , jail ))
	{
		return;
	}	

	point* aim = new point [5];
	point direction=ghostBefore;
	int count=0 , defally = 0 , defallx = 0;
	if(maze[ghost.x+1][ghost.y]==2 ||maze[ghost.x+1][ghost.y]==3 ||maze[ghost.x+1][ghost.y]==0 )
	{
		count++;
		aim[count]={1,0};
		defallx=1;
	}
	if(maze[ghost.x][ghost.y+1]==2 ||maze[ghost.x][ghost.y+1]==3 || maze[ghost.x][ghost.y+1]==0 )
	{
		count++;
		aim[count]={0,1};
		defally =1;
	}
	if(maze[ghost.x-1][ghost.y]==2 ||maze[ghost.x-1][ghost.y]==3 || maze[ghost.x-1][ghost.y]==0 )
	{
		count++;
		aim[count]={-1,0};
		defallx=1;
	}
	if(maze[ghost.x][ghost.y-1]==2 ||maze[ghost.x][ghost.y-1]==3 || maze[ghost.x][ghost.y-1]==0 )
	{
		count++;
		aim[count]={0,-1};
		defally=1;
	}
	if(count>2 || (count==2 && defallx==1 && defally == 1)) {
		int temp = (rand() % count) + 1;
		direction = aim[temp];
		ghostBefore = direction;
	}
	ghost.x+=direction.x;
	ghost.y+=direction.y;
	if(checkContradiction(ghost , ghostArr, pacman , superPower , hp , record ,numJail, pacmanRespawnPoint , ghostRespawnPoint , ghostBefore , ghostBeforeArr , jail))
	{
		return;
	}
	if(maze[ghost.x][ghost.y]==1)
	{
		ghost.x-=direction.x;
		ghost.y-=direction.y;
		return;
	}else if(maze[ghost.x][ghost.y]==-1)
	{
		ghost.x-=direction.x;
		ghost.y-=direction.y;
		return;
	}else 
	{
		for(int i = 1 ,count = 0; i <= 4 ;i++)
		{
			if((ghost.x==ghostArr[i].x) && (ghost.y==ghostArr[i].y))
			{
				count++;
			}
			if(count==2)
			{
				ghost.x-=direction.x;
				ghost.y-=direction.y;
				direction.x*=-1;
				direction.y*=-1;
				ghost.x+=direction.x;
				ghost.y+=direction.y;
				break;
			}
		}
	}
	if(maze[ghost.x][ghost.y]==1 || maze[ghost.x][ghost.y]==-1)
	{
		ghost.x-=direction.x;
		ghost.y-=direction.y;
	}
	delete [] aim;
	return;
}
void pacmanMove(point &pacman , point &direction , int &superPower , int &record  , point before)
{
	if(maze[pacman.x][pacman.y]==2)
	{
		record++;
		maze.change(pacman.x, pacman.y, 0);
	}else if(maze[pacman.x][pacman.y]==3)
	{
		record+=3;
		maze.change(pacman.x, pacman.y, 0);
		superPower=20;
	}
	pacman.x+=direction.x;
	pacman.y+=direction.y;
	if(maze[pacman.x][pacman.y]==1)
	{
		pacman.x-=direction.x;
		pacman.y-=direction.y;
		pacman.x+=before.x;
		pacman.y+=before.y;
		direction=before;
		if(maze[pacman.x][pacman.y]==1)
		{
			pacman.x-=before.x;
			pacman.y-=before.y;
		}
	}else if(maze[pacman.x][pacman.y]==2)
	{
		record++;
		maze.change(pacman.x, pacman.y, 0);
	}else if(maze[pacman.x][pacman.y]==-1)
	{
		pacman.x-=direction.x;
		pacman.y-=direction.y;
		pacman.x+=before.x;
		pacman.y+=before.y;
		direction=before;
		if(maze[pacman.x][pacman.y]==1)
		{
			pacman.x-=before.x;
			pacman.y-=before.y;
		}
	}else if(maze[pacman.x][pacman.y]==3)
	{
		record+=3;
		maze.change(pacman.x, pacman.y, 0);
		superPower=20;
	}
	return;
}
point input()
{
	point inp;
	char input;
	char nextInput;
	input = (char)getch();
	if(input == 'a')
	{
		inp.x=0;
		inp.y=-1;
	}else if(input == 'w')
	{
		inp.x=-1;
		inp.y=0;
	}
	else if(input == 's')
	{
		inp.x=1;
		inp.y=0;
	}else if(input == 'd')
	{
		inp.x=0;
		inp.y=1;
	}else if(input == 'e')
	{
		exit(0);
	}else if(input=='p')
	{
		input=(char )getch();
		inp.x=0;
		inp.y=0;
	}else
	{
		inp.x=0;
		inp.y=0;
	}
	return inp;
}

string chooseWallUnicode(int row, int col) {
	int rows = maze.size();
	int cols = maze[0].size();
	if (row == 0 && col == 0) {
		return "\u2554";
	} else if (row == rows-1 && col == 0) {
		return "\u255A";
	} else if (row == 0 && col == cols-1) {
		return "\u2557";
	} else if (row == rows-1 && col == cols-1) {
		return "\u255D";
	} else if (row == 0 || row == rows-1) {
		return "\u2550";
	} else if(col == 0 || col == cols-1) {
		return "\u2551";
	} else {
		int adjWallCount = 0;
		bool top = false;
		bool right = false;
		bool bottom = false;
		bool left = false;
		if(maze[row+1][col] == 1) {
			adjWallCount++;
			bottom = true;
		}
		if(maze[row-1][col] == 1) {
			adjWallCount++;
			top = true;
		}
		if(maze[row][col+1] == 1) {
			adjWallCount++;
			right = true;
		}
		if(maze[row][col-1] == 1) {
			adjWallCount++;
			left = true;
		}

		bool topRight = false;
		bool topLeft = false;
		bool bottomRight = false;
		bool bottomLeft = false;
		if(maze[row+1][col+1] == 2 || maze[row+1][col+1] == 0) {
			bottomRight = true;
		}
		if(maze[row-1][col-1] == 2 || maze[row-1][col-1] == 0) {
			topLeft = true;
		}
		if(maze[row-1][col+1] == 2 || maze[row-1][col+1] == 0) {
			topRight = true;
		}
		if(maze[row+1][col-1] == 2 || maze[row+1][col-1] == 0) {
			bottomLeft = true;
		}

		if(adjWallCount == 4) {
			if(topRight) {
				return "\u2514";
			} else if(topLeft) {
				return "\u2518";
			} else if(bottomRight) {
				return "\u250C";
			} else if(bottomLeft) {
				return "\u2510";
			} else {
				return " ";
			}
		} else if(top && bottom && (right || left)) {
			return "\u2502";
		} else if(right && left && (top || bottom)) {
			return "\u2500";
		} else if(right && top && !(left || bottom)) {
			return "\u2514";
		} else if( bottom && right && !(left || top)) {
			return "\u250C";
		} else if( left && bottom && !(right || top)) {
			return "\u2510";
		} else if( left && top && !(right || bottom)) {
			return "\u2518";
		} else {
			// other cases are gates
			return "\u2500";
		}
	}
}

bool print(int length,int height, point pacman , point ghost[] , int superPower , int record , int  hp)
{
	bool ret=0;
	string* temp = new string[length*height];

	#ifdef linux
		string ghostRep = "\u15E3";
		string pacmanRep = "\u15E7";
		string superPacmanRep = "\u2B24";
		string dotRep = "\u22C5";
		string superRep = "\u2630";
	#else
		string ghostRep = "☠";
		string pacmanRep = "◯";
		string superPacmanRep = "★";
		string dotRep = ".";
		string superRep = "☆";
	#endif
	
	for(int i = 0 ; i < length ; i++)
	{
		for(int j = 0 ; j < height ; j++)
		{
			if(maze[i][j]==0)
			{
				temp[i+j*length]=" ";
			}else if(maze[i][j]==1)
			{
				temp[i+j*length]=chooseWallUnicode(i, j);
			}else if(maze[i][j]==2)
			{
				ret=1;
				temp[i+j*length]=dotRep;
			}else if(maze[i][j]==3)
			{
				ret=1;
				temp[i+j*length]=superRep;
			}else if(maze[i][j]==-1)
			{
				temp[i+j*length]="\u254D";
			}
		}
	}

	if(superPower)
	{
		temp[pacman.x+pacman.y*length]=superPacmanRep;
		temp[ghost[1].x+ghost[1].y*length]=ghostRep;
		temp[ghost[2].x+ghost[2].y*length]=ghostRep;
		temp[ghost[3].x+ghost[3].y*length]=ghostRep;
		temp[ghost[4].x+ghost[4].y*length]=ghostRep;	
	}else{
		temp[pacman.x+pacman.y*length]=pacmanRep;
		temp[ghost[1].x+ghost[1].y*length]=ghostRep;
		temp[ghost[2].x+ghost[2].y*length]=ghostRep;
		temp[ghost[3].x+ghost[3].y*length]=ghostRep;
		temp[ghost[4].x+ghost[4].y*length]=ghostRep;
	}
	
	for(int i = 0 ; i < length ; i++)
	{
		for(int j = 0 ; j < height ; j++)
		{
			cout << temp[i+j*length];
		}
		cout<<endl;
	}
	cout<<"record: " << record << " HP: " << hp <<" superPower: " << superPower<<endl;

	delete[] temp;
	return ret;
}
bool move(int length,int  height ,point  pacmanRespawnPoint ,point ghostRespawnPoint ,int  level ,int& finalRecord)
{
	srand(time(0));
	string* win= new string [6];
	string* lose= new string [6];
	int hp=3,superPower=0,numJail=0,counterJail=0,record=0;
	int* jail = new int [5];
	point* ghostBefore=new point [5];
	point* ghost =new point [5];
	point pacman=pacmanRespawnPoint,direction={0,1};
	ghostBefore[1]={0,-1};
	ghostBefore[2]={0,1};
	ghostBefore[3]={0,-1};
	ghostBefore[4]={0,1};

	jail[1]=1;
	jail[2]=2;
	jail[3]=3;
	jail[4]=4;
	ghost[1].x=ghostRespawnPoint.x;
	ghost[1].y=ghostRespawnPoint.y;
	ghost[2].x=ghostRespawnPoint.x+1;
	ghost[2].y=ghostRespawnPoint.y;
	ghost[3].x=ghostRespawnPoint.x;
	ghost[3].y=ghostRespawnPoint.y+1;
	ghost[4].x=ghostRespawnPoint.x+1;
	ghost[4].y=ghostRespawnPoint.y+1;
	
	win[0]="Bravo! We knew you could do it.\n";
	win[1]="We could not be more proud of you.\n";
	win[2]="You are unstoppable.\n";
	win[3]="You are truly the GOAT.\n";
	win[4]="It’s been a long climb, but you aren't finally at the top. I am going to make a round that you can't win it.\n";
	
	lose[0]="Shame on you.\nYou have disappointed all the people of Pacman land, prepare yourself for a heavy punishment.\n";
	lose[1]="a little Shame on you.\nYou have disappointed JUST HALF OF people of Pacman land, prepare yourself for a heavy punishment.\n";
	lose[2]="May the curse of the Amon gods be upon you.\nprepare yourself for a torment.\n";
	lose[3]="Ihr Niveau ist zu niedrig.\n";
	lose[4]="My sense of creativity dried up, Condemn yourself according to your situation.\n";

	while(1)
	{
		int scaledRecord;
		if(level == 0) {
			scaledRecord = record;
		} else {
			scaledRecord = level*record;
		}
		if(!print(length, height , pacman , ghost , superPower , scaledRecord+finalRecord , hp))
		{
			break;
		}
		usleepNew(180000);
		#ifdef _WIN32
            printf("\033c");;
		#else
			system("clear");
		#endif
		point before=direction;
		int keys = kbhit();
		if(keys > 0)
		{
			for(int i = 0; i < keys; i++) {
				direction = input();
			}
			if(direction.x==0 && direction.y == 0 )
			{
				cout<<"invalid \n";
				continue;
			}
		}
		
		pacmanMove(pacman , direction, superPower ,record , before);

		for(int i = 1 ; i<= -1*numJail ; i++)
		{
			if(checkContradiction(ghost[i] , ghost , pacman , superPower , hp , record, numJail , pacmanRespawnPoint , ghostRespawnPoint ,ghostBefore[0] , ghostBefore , jail))
			{
				continue;
			}
		}

		if(level == 0) {
			scaledRecord = record;
		} else {
			scaledRecord = level*record;
		}
		if(!print(length, height , pacman , ghost ,superPower , scaledRecord+finalRecord ,hp))
		{
			break;
		}
		usleepNew(180000);
		#ifdef _WIN32
            printf("\033c");
		#else
			system("clear");
		#endif
		before=direction;
		keys = kbhit();
		if(keys > 0)
		{
			for(int i = 0; i < keys; i++) {
				direction = input();
			}
			if(direction.x==0 && direction.y == 0 )
			{
				cout<<"invalid \n";
				continue;
			}
		}
		pacmanMove(pacman , direction, superPower ,record , before);

		if(superPower>0)
		{
			superPower--;
		}
		counterJail++;

		if(numJail!=-4)
		{
			if(counterJail>=10-sqrt(level)) 
			{		
				counterJail=0;
				numJail--;
				int index;
				for ( int i = 4 ; i >=1 ; i-- )
				{
					if(jail[i]!= 0 )
					{
						index=jail[i];
						jail[i]=0;
						break;
					}
				}
				ghost[index]={ghostRespawnPoint.x-2,ghostRespawnPoint.y};
			}
		}
		
		for(int i = 1 ; i <= 4 ; i++ )
		{
			bool free=1;
			for( int j = 1 ; j <= 4 ; j++)
			{
				if( i == jail[j])
				{
					free=0;
					break;
				}
			}
			if(free)
			{
				if( level<=4)
				{
					if(i <= level)
					{
						ghostINTELEGENCEmove(ghost[i] ,  superPower,  pacman ,  ghost  , hp ,record , numJail , pacmanRespawnPoint , ghostRespawnPoint , ghostBefore[i] ,ghostBefore , jail );
					}else
					{
                        ghostMove(ghost[i], superPower, pacman, ghost, hp, record, numJail, pacmanRespawnPoint,
                                  ghostRespawnPoint, ghostBefore[i], ghostBefore, jail);
					}
				}else
				{
					if(i <= level-4)
					{
						superGhost(ghost[i] ,superPower, pacman , ghost , hp , record ,  numJail ,  pacmanRespawnPoint , ghostRespawnPoint , ghostBefore[i] , ghostBefore , jail );
					}else
					{
						ghostINTELEGENCEmove(ghost[i] ,  superPower,  pacman ,  ghost  , hp ,record , numJail , pacmanRespawnPoint , ghostRespawnPoint , ghostBefore[i] ,ghostBefore , jail );
					}
				}
			}
		}
		
		if(hp==0)
		{
			printString(lose[level/2]);
			sleep(3);
			if(level == 0) {
				finalRecord+=record;
			} else {
				finalRecord+=level*record;
			}
			return 0;
		}
	}
	printString(win[level/2]);
	sleep(3);
	if(level == 0) {
		finalRecord+=record;
	} else {
		finalRecord+=level*record;
	}

	delete[] win;
	delete[] lose;
	delete[] jail;
	delete[] ghostBefore;
	delete[] ghost;
	return 1;
}
