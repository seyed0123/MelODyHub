import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'dart:typed_data';
import 'package:path_provider/path_provider.dart';


import 'package:flutter/material.dart';


void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Flutter Demo',
      theme: ThemeData(
        // backgroundColor: CupertinoColors.systemIndigo,
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple,
            background: Colors.black12),
        useMaterial3: true,
      ),
      home: const Login(title:'title',),
    );
  }
}

class Login extends StatefulWidget {
  const Login({super.key, required this.title});

  final String title;

  @override
  State<Login> createState() => LoginState();
}
class LoginState extends State<Login> {
  final _formKey = GlobalKey<FormState>();
  TextEditingController usernameController = TextEditingController();
  TextEditingController passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.deepPurpleAccent,
        title: Text(widget.title,
        ),
      ),
      body: Form(
        key: _formKey,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Image.asset(
              'pictures/melodyhub.jpg',
              width: 300.0,
              height: 300.0,
            ),
            SizedBox(height: 16.0),
            Padding(
              padding:
              const EdgeInsets.symmetric(horizontal: 8, vertical: 16),
              child: TextFormField(
                controller: usernameController,
                decoration: const InputDecoration(
                    border: OutlineInputBorder(), labelText: "Username"),
                validator: (value) {
                  if (value == null || value.isEmpty || value.compareTo('OXDMamad')!=0) {
                    return 'Please enter valid username';
                  }
                  return null;
                },
              ),
            ),
            Padding(
              padding:
              const EdgeInsets.symmetric(horizontal: 8, vertical: 16),
              child: TextFormField(
                controller: passwordController,
                obscureText: true,
                decoration: const InputDecoration(
                    border: OutlineInputBorder(), labelText: "Password"),
                validator: (value) {
                  if (value == null || value.isEmpty || value.compareTo('zxc')!=0) {
                    return 'Please enter valid password';
                  }
                  return null;
                },
              ),
            ),
            Padding(
              padding:
              const EdgeInsets.symmetric(horizontal: 8, vertical: 16.0),
              child: Center(
                child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children : [ElevatedButton(
                      onPressed: () {
                        if (_formKey.currentState!.validate()) {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (context) => HomePage(
                                  username: usernameController.text,
                                )),
                          );
                          // Navigate the user to the Home page
                        } else {
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(content: Text('Please fill input correctly')),
                          );
                        }
                      },
                      child: const Text('Login'),
                    ),
                    ]),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
class HomePage extends StatefulWidget {
  final String username;
  // List<String> myList;

  const HomePage({super.key, required this.username});
  @override
  State<StatefulWidget> createState() {
    return _MyAppState();
  }
}


class _MyAppState extends State<HomePage> with SingleTickerProviderStateMixin {
  late TabController _tabController;
  late List list1=[' 2324be7a-f413-499f-ad12-361d8a0280d1 | johndoe | johndoe@example.com | 1234567890 | male | null | null | null | null | null | null | 1990-01-01 |','| 2f645e57-e7f8-4254-95ce-ad94e4f20a79 | janedoe| janedoe@example.com | 0987654321 | female | null | null | null | null | null | null | 1995-05-05 |',' 6f51a2cb-feff-4ec7-a35b-928c1193b561 | seyed | seyed123ali123@gmail.com | 12334656 | male | null | null | null | \[\] | \[\] | true | 2000-09-12 |', 'a7be8525-fbba-46ad-be12-20baea66f119 | jamshid | sabamadadi9@gmail.com | +989911502620 | male | null | null | null | null | null | true | 1995-06-16 |'];
  late List list2=['| 3da18868-1566-46d6-8248-f1a4bfc961c5 | erfan | arshanelmtalab@gmail.com | 65468654654 |  | false |  | null | null | 0 | 0 | null |','| 5ec270c1-874a-43b2-8968-545aef2dfaee | john | john@example.com | 12345678 |  | false |  | null | null | 0 | 0 | null |','| c2d2a273-f455-4a96-910e-f738a8d664b6 | lisa | lisa@example.com | 87654321 |  | false |  | null | null | 0 | 0 | null |'];
  late List list3=['| 576hi9LB18JqEJssX8QV9E | mamad on mars | dance | 4 | 1988 | 80 | Youre the one that I want, the one that I need<br/>Youre the light in my darkness, the air that I breathe<br/>Together were unstoppable, our loves a symphony<br/>Forever and always, you and me | null | null |','| 5LtQ6ATUs5MdV31jF794jg | mamad in moon | pop | 5 | 2023 | 100 | I love the way you move, the way you dance<br/>You make me lose control, you take a chance<br/>When were together, time stands still<br/>Youre the missing piece, you complete the thrill | null | null |','| 140b409c-0184-48d4-b55b-0050c4f40523 | Autumn Leaves | jazz | 3.5 | 2022 | 0 |  | null | null |'];
  late int index=1 ;


  @override
   initState() {
    super.initState();
    _tabController = TabController(length: 6, vsync: this, initialIndex: 0);
    _initializeAsyncData();
  }
  void _initializeAsyncData() async {
  }
  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  mybutton(String text) {
    int n;
    if (text == '->')
      n = 1;
    else
      n = -1;

    return OutlinedButton(style: ButtonStyle(
      foregroundColor: MaterialStateProperty.all<Color>(Colors.blue),
      backgroundColor: MaterialStateProperty.all<Color>(Colors.red),
    ), onPressed: (){
      index += n;
      setState(() {
      })
      ;
      }, child: Text(text));
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        backgroundColor: Colors.green,
        appBar: AppBar(
          title: Text('Tab Bar Example'),
          bottom: TabBar(
            controller: _tabController,
            tabs: [
              for (int i = 0; i < 3; i++)
              Tab(text: 'Tab ${i}'),
            ],
          ),
        ),
        body: TabBarView(
          controller: _tabController,
          children: [
            Center(
              child: Column(
                children: list1.map((item) => Center(child: Text(item , style: TextStyle( fontSize:30,color: Colors.black ),))).toList(),
              ),
            ),
            Center(
              child: Column(
                children: list2.map((item) => Center(child: Text(item , style: TextStyle( fontSize:30,color: Colors.black ),))).toList(),
              ),
            ),
            Center(
              child: Column(
                children: list3.map((item) => Center(child: Text(item , style: TextStyle( fontSize:30,color: Colors.black ),))).toList(),
              ),
            ),
            Center(
              child: Text('Tab 4 Content'),
            ),
            Center(
              child: Text('Tab 5 Content'),
            ),
            Center(
              child: Column(
                children: [
                  Row(
                    // crossAxisAlignment: CrossAxisAlignment.,
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    mybutton('<-'),
                    mybutton('->')
                  ],
                ),
                Text(list1[index]),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}


