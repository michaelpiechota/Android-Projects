I used Google's Camera API for all aspects of this project. 
Here is the link to the documentation: https://developer.android.com/guide/topics/media/camera.html

The Camera API provides access to both the front and back facing camera along with 
a built-in features for face recognition. I used the starting code from the above documentation
for such classes as class MyFaceDetectionListener implements Camera.FaceDetectionListener {}
and startFaceDectection{}. The documentation provided me help with implementing the methods I needed. 
The rest was simply setting up my if then statements for the parameters I specified as "yes" or "no". 
The response was displayed via textview at the bottom of the application itself and is updated in real time. 