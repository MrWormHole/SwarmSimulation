import hype.*;
import hype.extended.behavior.HSwarm;
import hype.extended.behavior.HTimer;
import hype.extended.colorist.HColorPool;
import java.awt.*;

HSwarm[]      mySwarmArr;
HSwarm        mySwarm;
HRect         myRect;
HColorPool    myColPool;
HDrawablePool myObjPool;
HTimer        myTimer;
int swarmCount = 1000;
int i = 0;
int destinationX = 320;
int destinationY = 320;
int startingX = 500;
int startingY = 100;

void setup() {
  size(640,640);
  H.init(this).background(#2980b9);
  mySwarmArr = new HSwarm[swarmCount];
  mySwarm = new HSwarm().addGoal(destinationX,destinationY).speed(5).turnEase(0.1f).twitch(20);
  myRect = (HRect)new HRect().rounding(8).size(20,10);
  myColPool = new HColorPool(#9b59b6, #f39c12, #ffffff).fillOnly();
  myObjPool = new HDrawablePool(swarmCount);
  myObjPool.autoAddToStage()
    .add(myRect)
    .colorist(myColPool)
    .onCreate(
      new HCallback() {
        public void run(Object obj) {
          HDrawable startingPoint = (HDrawable) obj;
          startingPoint.noStroke().loc(startingX, startingY).anchorAt(H.CENTER);
          mySwarm.addTarget(startingPoint);
          mySwarmArr[i] = mySwarm;
          i++;
        }
      });

  myTimer = new HTimer()
    .numCycles( myObjPool.numActive() )
    .interval(250)
    .callback(
      new HCallback() { 
        public void run(Object obj) {
          myObjPool.request();
        }
      });
}

void draw() {
  H.drawStage();
  noFill(); strokeWeight(2); stroke(#000000);
  ellipse(startingX, startingY, 6, 6);
  noFill(); strokeWeight(2); stroke(#FF3300);
  ellipse(destinationX, destinationY, 6, 6);
}

void keyPressed(){
  if(key == 's'){
   takeScreenShotAndSave();
   System.out.println("New screenshot has been recorded");
  }
}

void mousePressed(){
 if(mouseButton == LEFT){
  setNewStartingPoint(); 
  System.out.println("New starting point has been set");
 }
 if(mouseButton == RIGHT){
   setNewDestinationPoint(); 
   System.out.println("New destination point has been set");
 }
}

void takeScreenShotAndSave(){
  save("screenshot.png");
}

void setNewDestinationPoint(){
  destinationX = mouseX;
  destinationY = mouseY;
  for(HSwarm s : mySwarmArr){
    if( s != null){
        s.removeGoal(s.goals().removeAt(0));
    }
    destinationX = mouseX;
    destinationY = mouseY;
    if( s != null){
      s.addGoal(destinationX,destinationY);
    }
  }
}

void setNewStartingPoint(){
  startingX = mouseX;
  startingY = mouseY;
}
