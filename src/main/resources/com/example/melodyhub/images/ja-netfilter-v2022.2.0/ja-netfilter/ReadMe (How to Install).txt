╔═══╗─────╔═╗─╔╗─╔═══╗╔═══╗────╔═══╗
║╔═╗║─────║╔╝╔╝╚╗║╔═╗║║╔═╗║────║╔═╗║
║╚══╗╔══╗╔╝╚╗╚╗╔╝║╚═╝║║╚═╝║──╔╗║╚═╝║
╚══╗║║╔╗║╚╗╔╝─║║─╚══╗║║╔═╗║──╠╣║╔╗╔╝
║╚═╝║║╚╝║─║║──║╚╗╔══╝║║╚═╝║╔╗║║║║║╚╗
╚═══╝╚══╝─╚╝──╚═╝╚═══╝╚═══╝╚╝╚╝╚╝╚═╝
    ┌ ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀ ┐
             Soft98.iR ™
    └ ▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄ ┘


1. Install JetBrains
2. Download crack
3. For windows : Copy folder ja-netfilter to Drive C:
4: Auto configure vmoptions:

- Windows: double click to execute 
"scripts\install-current-user.vbs" (For current user)
"scripts\install-all-users.vbs" (For all users)

- macOS or Linux: execute  (Copy form Neo) 
"scripts/install.sh"

or

Manually Edit:
Go to install directory product JetBrains ( Example : C:\Program Files\JetBrains\PhpStorm 2022.3\bin ) find *.exe.vmoptions ( Example : phpstorm64.exe.vmoptions ) open with Notepad++ add end line & Save:

windows:
-javaagent:C:\ja-netfilter\ja-netfilter.jar=jetbrains
--add-opens=java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED
--add-opens=java.base/jdk.internal.org.objectweb.asm.tree=ALL-UNNAMED

mac:
-javaagent:/Users/x/ja-netfilter/ja-netfilter.jar=jetbrains
--add-opens=java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED
--add-opens=java.base/jdk.internal.org.objectweb.asm.tree=ALL-UNNAMED

linux:
-javaagent:/home/x/ja-netfilter/ja-netfilter.jar=jetbrains
--add-opens=java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED
--add-opens=java.base/jdk.internal.org.objectweb.asm.tree=ALL-UNNAMED

( Instead of "x" enter user name )

5. open JetBrains and use license server and Active ( https://jetbra.in )

or

copy code to Activation Code and Active