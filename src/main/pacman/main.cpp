#include "move.cpp"


int main()
{
	int width;
	int height;
	string username;
	int finalRecord=0;
	int level=0;

	showMenu(username, level, width, height, finalRecord);

	width -= width % 2;
	ofstream recordsFile;
	recordsFile.open("ranks.pacman", ios::app);

	for(; level <= 8 ; level+=2 )
	{
		printString("please wait a second.\nthe game is being prepared.\n");
		sleep(2);
		maze = createDesirableMaze(width, height);
		point pacmanRespawnPoint={height-2,width/2},ghostRespawnPoint={(height-3)/2-1,width/2-1};
		bool nonDefeat=move(height,width,pacmanRespawnPoint , ghostRespawnPoint , level , finalRecord);

		if(nonDefeat==0)
		{
			break;
		}else
		{
			string confirmation;
			sevenSegment("chechpoint",2);
			printString("\ndo yo want to save the game\nyou can oonly save game on checkpoints.\n y/n");
			cin>>confirmation;
			if(confirmation=="y")
			{
				save(username, level+2 ,width , height , finalRecord);
			}
		}
	}
	if(recordsFile.is_open()) {
		recordsFile<< username << ":" << finalRecord<<endl;
		recordsFile.close();
	}

	return 0;
}
