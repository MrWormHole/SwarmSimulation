import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import hype.*; 
import hype.extended.behavior.HSwarm; 
import hype.extended.behavior.HTimer; 
import hype.extended.colorist.HColorPool; 
import java.awt.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SwarmSimulation extends PApplet {







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

public void setup() {
  
  H.init(this).background(0xff2980b9);
  mySwarmArr = new HSwarm[swarmCount];
  mySwarm = new HSwarm().addGoal(destinationX,destinationY).speed(5).turnEase(0.1f).twitch(20);
  myRect = (HRect)new HRect().rounding(8).size(20,10);
  myColPool = new HColorPool(0xff9b59b6, 0xfff39c12, 0xffffffff).fillOnly();
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

public void draw() {
  H.drawStage();
  noFill(); strokeWeight(2); stroke(0xff000000);
  ellipse(startingX, startingY, 6, 6);
  noFill(); strokeWeight(2); stroke(0xffFF3300);
  ellipse(destinationX, destinationY, 6, 6);
}

public void keyPressed(){
  if(key == 's'){
   takeScreenShotAndSave();
   System.out.println("New screenshot has been recorded");
  }
}

public void mousePressed(){
 if(mouseButton == LEFT){
  setNewStartingPoint(); 
  System.out.println("New starting point has been set");
 }
 if(mouseButton == RIGHT){
   setNewDestinationPoint(); 
   System.out.println("New destination point has been set");
 }
}

public void takeScreenShotAndSave(){
  save("screenshot.png");
}

public void setNewDestinationPoint(){
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

public void setNewStartingPoint(){
  startingX = mouseX;
  startingY = mouseY;
}
  public void settings() {  size(640,640); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "SwarmSimulation" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
