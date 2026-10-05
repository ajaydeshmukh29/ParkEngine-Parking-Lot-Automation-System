#ParkEngine - Parking_Lot_Management_System using Design Patterns

Parking lot automation system in Java.

- **Part 1 (this version):** console app + AWT/Swing GUI on top of the same classes
- Design patterns: Factory, Observer, Strategy (parking / pricing / payment), Singleton
- Features: park vehicle, exit with billing + payment, search vehicle, live display boards

## Project structure
```
src/
  program1017.java       original console app (unchanged)
  ParkingLotSetup.java   builds floors, spots, boards, gates for the GUI
  TextAreaStream.java    redirects System.out into the GUI log
  ParkEngineFrame.java   main window (AWT layouts + events, Swing components)
  ParkEngineApp.java     GUI entry point
```

## Run
```
javac -d bin src/*.java
java -cp bin ParkEngineApp     # GUI
java -cp bin program1017       # console
```
