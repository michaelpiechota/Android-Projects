Michael Piechota
Project 6 Description

My enhancement: This program changes the color of the line depending on which
floor you are on.

Parameters:
-This program uses the barometer to sense pressure differences between floors.
-This program assumes that when you open the application, you are on the ground floor.
  -This is due to the barometric pressure being influenced by weather and atmospheric conditions.
   Therefore, it is impossible to know which floor you are on using the barometer.
   That is why this application makes that assumption and uses the pressure value for the ground floor in the calculations.

Calculations:
The function findFloor() determines the floor number:
private void findFloor(float newPressure, float firstPressure){
    if (newPressure < firstPressure ){
        if (newPressure < (firstPressure - 0.20) && newPressure > (firstPressure - 0.40)){
            currentFloor = 2;
        }
        else if (newPressure < (firstPressure - 0.40) && newPressure > (firstPressure - 0.60)){
            currentFloor = 3;
        }
        else if (newPressure < (firstPressure - 0.60)){
            currentFloor = 4;
        }
    }
    //redrawLine();
}

Through personal testing, I have determined the differences of .20 to be the difference adequate to distinguish between floors.
Therefore, through a simple if/else if statement, I am able to create cases for the floors and change the value of currentFloor.
I then use the value currentFloor in my re-draw function which handles the drawing of the line.
