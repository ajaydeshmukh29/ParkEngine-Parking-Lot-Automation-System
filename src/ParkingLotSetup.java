/*
    ParkingLotSetup
    ---------------
    Your original main() builds the parking lot inside itself, so we
    cannot reuse it from the GUI. This class repeats the same setup
    (2 floors, 6 spots each, display boards, gates) WITHOUT touching
    program1017.java.

    ParkingLot is a Singleton, so GUI and console logic share the same
    object model - we only call your existing methods.
*/
class ParkingLotSetup
{
    final ParkingLot parkingLot;
    final EntryGate entryGate;
    final ExitGate exitGate;

    ParkingLotSetup()
    {
        parkingLot = ParkingLot.getInstance();
        parkingLot.setParkingLotName("Marvellous ParkEngine");

        // Floor 1
        ParkingFloor floor1 = new ParkingFloor(1);
        floor1.addParkingSpot(new BikeSpot(101));
        floor1.addParkingSpot(new BikeSpot(102));
        floor1.addParkingSpot(new CarSpot(103));
        floor1.addParkingSpot(new CarSpot(104));
        floor1.addParkingSpot(new TruckSpot(105));
        floor1.addParkingSpot(new TruckSpot(106));
        floor1.addObserver(new ParkingDispalyBoard(floor1));

        // Floor 2
        ParkingFloor floor2 = new ParkingFloor(2);
        floor2.addParkingSpot(new BikeSpot(201));
        floor2.addParkingSpot(new BikeSpot(202));
        floor2.addParkingSpot(new CarSpot(203));
        floor2.addParkingSpot(new CarSpot(204));
        floor2.addParkingSpot(new TruckSpot(205));
        floor2.addParkingSpot(new TruckSpot(206));
        floor2.addObserver(new ParkingDispalyBoard(floor2));

        parkingLot.addFloor(floor1);
        parkingLot.addFloor(floor2);

        entryGate = new EntryGate(1);
        exitGate = new ExitGate(1);
    }
}
