# 🚢 Battleship Arcade – Java Edition 🎮

>Welcome to **Battleship Arcade**, a stylish and colorful rendition of the classic naval combat game built entirely in **Java Swing**! Get ready to place your fleet and engage in strategic warfare against the AI in a grid-based showdown. ⚓💥

---

## 📦 Project Structure

```bash
Battleship_Java_vSimple/        
├── gui/                   # 🖥️ GUI components and visual interface
│   └── BattleShipsGUI.java      # 🎨 Main game window, handles user interaction
├── logic/                 # 🧠 Core game logic and rules
│   ├── GameController.java      # 🎮 Game flow controller, manages turns and rules
│   ├── Board.java               # 🗺️ Board representation, stores ship positions & hits
│   └── ShipPlacer.java          # 🚢 Logic for placing ships on the board
├── util/                  # 🛠️ Utility classes and constants
│   └── Constants.java           # 📏 Game constants (grid size, ship count, etc.)
│ 
├── Main.java  # 🎬 Entry point, starts the game
├── README.md  # 📄 Project documentation.
└── LICENSE    # 📝 MIT License.

```

---

## 🕹️ Gameplay

The objective is simple: **sink all 5 enemy ships** before the computer sinks yours.

### 🛳️ Ship Details

- You and the computer each have **5 ships**.
- Each ship occupies **1 single cell** on a 10×10 grid.
- You must manually place your ships before the game begins.

---

### 🧩 Symbols Guide

| Symbol | Meaning                          |
|--------|----------------------------------|
| `■`    | Your ship                        |
| `x`    | Hit (either yours or enemy's)    |
| `☠`    | Sunk ship                        |
| `≈`    | Missed shot                      |

---

## 🎨 Interface

- 🛡️ **Left Grid:** Your ships.
- 🎯 **Right Grid:** Where you attack enemy positions.
- Colorful buttons and Unicode emojis enhance readability and fun!

---

## 🚀 How to Run

### ✅ Prerequisites
>Make sure you have **Java JDK 8** or higher installed on your system.

### 📥 Clone the repository
> If you have Git installed, open your terminal and type:

```bash
git clone https://github.com/andredisa/Battleship_Java_vSimple.git
cd Battleship_Java_vSimple
```


### 🔧 Compiling the Project
>Open your terminal and navigate to the root project folder (`Battleship_Java_vSimple/`), then run:

```bash
javac -d out Main.java gui/*.java logic/*.java util/*.java
```
> This command compiles all Java source files and places the generated .class files into the out/ directory.

### ▶️ Launching the Game
```bash
java -cp out Main
```

---

## 🧠 How to Play

### 1️⃣ Place Your Ships:
>Click 5 times on your grid (left side) to place your ships `(■)`. You can’t overlap.

### 2️⃣ Start the Battle:
>Once all ships are placed, the AI places its own ships randomly.

### Take Turns:
> Click on the enemy grid (right side) to fire.
> The computer will respond with its move after yours.

### Win or Lose:
>🏆 **Win**: Sink all 5 enemy ships first.

>💀 **Lose**: The computer sinks all your ships first.

---

## 🧼 Reset / Exit
>Once the game ends (win or lose), a message is shown and the app closes automatically.

---

## 💡 Features
- 🎨 Clean, emoji-rich GUI with color themes
- 🤖 Simple AI logic for ship placement and random firing
- 🎯 Visual feedback for each hit, miss, or sunken ship
- 🧪 Modular code structure for easy maintenance and expansion

---

## 🔄 Future Enhancements & Ideas

> Here are some exciting features planned to take the game to the next level:

- 🚢 **Multi-cell Ships:** Introduce ships spanning multiple cells (e.g., length 2 to 4) for added strategic depth.
- 🎮 **Multiplayer Mode:** Enable hotseat or LAN multiplayer battles to challenge friends.
- 🤖 **AI Difficulty Levels:** Implement adjustable difficulty settings to offer beginners and veterans a suitable challenge.
- 🔊 **Sound Effects & Animations:** Add immersive audio cues and smooth animations for hits, misses, and ship sinking.
- 🌐 **Online Leaderboards:** Track player scores and rankings globally.
- 🖥️ **Improved UI/UX:** Enhance interface responsiveness and accessibility options.
- 💾 **Save & Load Games:** Allow players to save progress and resume battles later.

---

## 🎉 Try it Out!

Feel free to **fork** or **clone** this repo!  
Perfect for learning Java GUI programming and having fun! 😄

---

## 📝 License

This project is open-source and free to use. Enjoy! 🚀

---

## ☕ Support Me

If you find my work useful and would like to support me, you can buy me a coffee! Your support helps me keep creating and improving my projects. Thank you! 😊

<a href="https://www.buymeacoffee.com/andredisa" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me A Coffee" style="height: 60px !important;width: 217px !important;" ></a>

---

**Enjoy sinking ships! 🌊🔥 If you like it, give it a ⭐!**