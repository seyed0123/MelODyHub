#include <iostream>
#include <string>


using namespace std;
void printString(string n)
{
	for(int i =0 ; i <n.size() ; i++ )
	{
		cout<<n[i];
		if(n[i]=='.')
		{
			sleep(2);
		}else
			usleep(60000);
	}
}
void sevenSegment(string n , int b)
{
    for(int j=1;j<=2*b+3;j++)
    {
        for(int i=0;i<n.size()*((2*b)+4);i++)
        {
        	
            int index=i/(2*b+4);
            char number=n[index];
            if(j==1)
            {
                if(i%(2*b+4)<1||i%(2*b+4)>2*b+2)
                    cout<<' ';
                else if((number=='0' || number=='2' || number=='3' || number=='5' || number=='6' || number=='7' || number=='8' || number=='9' || number=='a' || number=='c' || number=='e' || number=='f' || number=='g' || number=='k' || number=='m' || number=='p' || number=='q' || number=='s' || number=='z' ) && (i%(2*b+4))%2==0)
                {
                    cout<<"\u2500";
                }else
                {
                    cout<<' ';
                }
            }
            else if(j==2*b+3)
            {
                if(i%(2*b+4)<1||i%(2*b+4)>2*b+2)
                    cout<<' ';
                else if((number=='0'||number=='2'||number=='3'||number=='5'||number=='6' ||number=='8'||number=='9' || number=='b' || number=='c' || number=='d' || number=='e' || number=='g' || number=='j' || number=='l' || number=='o' || number=='s' || number=='t' || number=='u' || number=='v' || number=='w' || number=='y' || number=='z') && (i%(2*b+4))%2==0)
                {
                    cout<<"\u2500";
                }else
                {
                    cout<<' ';
                }
            }
            else if(j<b+2)
            {
                if(i%(2*b+4)!=1 && i%(2*b+4)!=2*b+3 )
                    cout<<' ';
                else if((number=='0'||number=='4'||number=='6'||number=='8'||number=='9' || number == '5' || number=='a' || number=='b' || number=='c' || number=='e' || number=='f' || number=='g' || number=='h' || number=='k' || number=='l' || number=='p' || number=='q' || number=='s' || number=='t' || number=='u' || number=='w' || number=='x' || number=='y') && i%(2*b+4)==1)
                    cout<<'|';
                else if((number=='0'||number=='1'||number=='2'||number=='3'||number=='4'||number=='7'||number=='8'||number=='9' || number=='a' || number=='d' || number=='j' || number=='p' || number=='q' || number=='u' || number=='w' || number=='x' || number=='y' || number=='z')&& (i%(2*b+4)==2*b+3))
                    cout<<'|';
                else
                    cout<<' ';
            }
            else if(j>b+2)
            {
                if(i%(2*b+4)!=1 && i%(2*b+4)!=2*b+3 )
                    cout<<' ';
                else if((number=='0'||number=='2'||number=='6'||number=='8' || number=='a' || number=='b' || number=='c' || number=='d' || number=='e' || number=='f' || number=='g' || number=='h' || number=='j' || number=='k' || number=='l' || number=='m' || number=='n' || number=='o' || number=='p' || number=='r' || number=='t' || number=='u'|| number=='v'|| number=='x'|| number=='z')&&i%(2*b+4)==1)
                    cout<<'|';
                else if((number=='0'||number=='1'||number=='3'||number=='4'||number=='5'||number=='6'||number=='7'||number=='8'||number=='9'|| number=='a'|| number=='b'|| number=='d'|| number=='g'|| number=='h'|| number=='i'|| number=='j'|| number=='k'|| number=='m' || number=='n'|| number=='o'|| number=='q'|| number=='s'|| number=='u'|| number=='v'|| number=='x'|| number=='y')&& i%(2*b+4)==2*b+3 )
                    cout<<'|';
                else
                    cout<<' ';
            }
            else if(j==b+2)
            {
                if(i%(2*b+4)<1||i%(2*b+4)>2*b+2)
                    cout<<' ';
                else if((number=='4'||number=='2'||number=='3'||number=='5'||number=='6'||number=='8'||number=='9'|| number=='a'|| number=='b'|| number=='d'|| number=='e'|| number=='f'|| number=='h'|| number=='k'|| number=='m'|| number=='n'|| number=='o'|| number=='p'|| number=='q'|| number=='r'|| number=='s'|| number=='t'|| number=='w'|| number=='x'|| number=='y'|| number=='z')&& (i%(2*b+4))%2==0)
                {
                    cout<<"\u2500";
                }else
                {
                    cout<<' ';
                }
            }

        }
        cout<<endl;
    }
    //cin>>n;
}