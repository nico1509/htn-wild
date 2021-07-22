package edu.kit.aifb.ls3.kaefer.jmonkeyld;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.HttpMethod;
import org.semanticweb.yars.nx.Nodes;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.UrlLocator;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

import edu.kit.aifb.ls3.kaefer.jmonkeyld.model.HUDmessage;
import edu.kit.aifb.ls3.kaefer.jmonkeyld.model.POI;
import edu.kit.aifb.ls3.kaefer.jmonkeyld.model.WebMap;

@WebListener
public class ContextListenerApp extends SimpleApplication implements ServletContextListener, AnimEventListener {

	static enum TransformationType {
		parent, model, world
	}

	static enum Direction {
		North, South, East, West
	}

	// TODO: Add object name here:
	static final String CAMERA = "camera";
	static final String NODE = "node-";
	static final String USER = "user";
	static final String KINECT = "kinect";
	static final String MAP = "map";
	// added arm here
	static final String ARM = "arm";
	static final String STATES = "states";
	// added conveyor belt here
	static final String BELT = "belt";
	static final String BSTATES = "bstates";
	static final String ARMUP = "armupdates";
	static final String MACHINEUP = "machineupdate";
	// added channel here
	static final String CHANNEL = "channel";
	static final String BCHANNEL = "bchannel";
	static final String CONTROL = "control";
	static final String BCONTROL = "bcontrol";
	static final String SKELCONTROL = "skeletonControl";
	static final String ANIMS = "anims";
	static final String BANIMS = "banims";
	static final String ROOTNODE = "rootnode";
	static final String PRODUCTS = "products";
	static final String MACHINES = "machines";
	static final String MSTATES = "mstates";
	static final String MACTION = "maction";
	static final String SSTATES = "sstates";
	static final String SACTION = "saction";
	static final String STORAGE = "storage";
	static final String STORAGECOUNT = "storageCount";
	static final String STORAGEUP = "storageupdate";
	

	static final String GEOMETRY = "geometry-";
	static final String SKELETON = null;
	static final String BONE = null;
	static final String BONENAMES = null;
	static final String TRANSFORMS = "transforms";
	static final String POIS = "POIS";
	static final String POIhighlighter = "POIhighlighter";
	static final String loadingPOIs = "loadingPOIs";
	private static final double marginRatio = 0.125;
	static final String tempResources = "tempResources";
	public static final String POIsHash = "world-poi-cache-hash";
	public static final String POIsCACHE = "world-poi-cache-content";
	public static final String hudmessage = "hudmessage";

	public static final String postLabel = "tweet";
	public static final String postImage = "Textures/POI/twitter.png";

	public static final ColorRGBA britishRacingGreen = new ColorRGBA(0.1058823f, 0.301960f, 0.2431372f, 1);

	float scalingFactor = 10;
	// cam.setLocation(new Vector3f(0, 16, 50));
	int textLinesInHudEvent = 1;
	int textLinesInHudPost = 3;

	static int quadDimension = 128;

	int poiPicSize = 252;
	String lastFmPicUriPrefix = "http://userserve-ak.last.fm/serve/" + poiPicSize + "/";

	String eventfulUriPrefix = "http://s1.evcdn.com/images/";
	String eventfulUriPrefixSuffix = ".evcdn.com/images/";

	URI mapUri;

	WebMap _currentGoogleMap;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	Map<String, POI> _pois = new ConcurrentHashMap();

	Map<Integer, Map<Integer, Node>> products = new Hashtable<Integer, Map<Integer, Node>>();

	Map<Integer, Node> belts = new Hashtable<Integer, Node>();

	Map<Integer, Node> arms = new Hashtable<Integer, Node>();

	Map<Integer, Node> machines = new Hashtable<Integer, Node>();

	Map<Integer, String> _armupdate = new ConcurrentHashMap<Integer, String>();
	Map<Integer, String> _machineupdate = new ConcurrentHashMap<Integer, String>();
	Map<Integer, String> _storageupdate = new ConcurrentHashMap<Integer, String>();
	Map<Integer, Integer> storageCount = new Hashtable<Integer, Integer>();
	Map<Integer, SkeletonControl> skelContr = new Hashtable<Integer, SkeletonControl>();
	Map<Integer, String> anims = new Hashtable<Integer, String>();

	Map<Integer, String> mstates = new Hashtable<Integer, String>();
	Map<Integer, String> maction = new Hashtable<Integer, String>();
	
	Map<Integer, String> sstates = new Hashtable<Integer, String>();
	Map<Integer, String> saction = new Hashtable<Integer, String>();

	HUDmessage _hum;

	// add ServletContext in order to talk with RESTInterface - NEEDS TO STAY!
	ServletContext _ctx;

	public void contextInitialized(ServletContextEvent sce) {

		_ctx = sce.getServletContext();

		// Register Servlet
		ServletRegistration sr = _ctx.addServlet("JMonkey Linked Data REST Interface",
				org.glassfish.jersey.servlet.ServletContainer.class);
		sr.addMapping("/*");
		sr.setInitParameter(org.glassfish.jersey.server.ServerProperties.PROVIDER_PACKAGES,
				this.getClass().getPackage().getName() + ","
						+ org.semanticweb.yars.jaxrs.JerseyAutoDiscoverable.class.getPackage().getName());

		FilterRegistration fr;
		// Register and configure filter to handle CORS requests
		fr = _ctx.addFilter("cross-origin", org.eclipse.jetty.servlets.CrossOriginFilter.class.getName());
		fr.setInitParameter(org.eclipse.jetty.servlets.CrossOriginFilter.ALLOWED_METHODS_PARAM,
				HttpMethod.GET + "," + HttpMethod.PUT + "," + HttpMethod.POST + "," + HttpMethod.DELETE);
		fr.addMappingForUrlPatterns(null, true, "/*");

		_ctx.setAttribute(tempResources, new ConcurrentHashMap<String, Set<Nodes>>());

		this.start();
	}

	public void contextDestroyed(ServletContextEvent arg0) {

		this.stop();
	}

	public void simpleInitApp() {
		/*
		 * Sky.
		 */
		Spatial skybox;
		Texture solidColor = assetManager.loadTexture("Textures/Sky/SolidCyan/square.png");
		skybox = SkyFactory.createSky(assetManager, solidColor, solidColor, solidColor, solidColor, solidColor,
				solidColor);
		rootNode.attachChild(skybox);
		_ctx.setAttribute(ROOTNODE, rootNode);

		// TODO:
		_ctx.setAttribute(loadingPOIs, Boolean.TRUE);

		Node kinectNode = new Node(KINECT);
		_ctx.setAttribute(NODE + KINECT, kinectNode);
		_ctx.setAttribute(loadingPOIs, Boolean.FALSE);

		Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat1.setColor("Color", ColorRGBA.Blue);

		/*
		 * Creating the Quad for the Map
		 */

		Quad q = new Quad(quadDimension, quadDimension);
		Geometry geom = new Geometry("surface", q);

		// creating the Quads for the shaded corners of the map
		Quad[] qs = new Quad[Direction.values().length];
		Geometry[] gs = new Geometry[Direction.values().length];
		Material transpMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		transpMat.setColor("Color", new ColorRGBA(1f, 1f, 0f, 0.3f));
		transpMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		for (Direction d : Direction.values()) {
			// creating and sizing the Quads
			switch (d) {
			case East:
			case West:
				qs[d.ordinal()] = new Quad((float) (quadDimension * marginRatio),
						(float) (quadDimension - (2f * marginRatio * quadDimension)));
				break;
			case North:
			case South:
				qs[d.ordinal()] = new Quad((float) (quadDimension), (float) (quadDimension * marginRatio));
				break;
			}

			gs[d.ordinal()] = new Geometry(d.name(), qs[d.ordinal()]);
			gs[d.ordinal()].setMaterial(transpMat.clone());
			gs[d.ordinal()].setQueueBucket(Bucket.Transparent);

		}

		Set<Geometry> geometriesToBeMoved = new HashSet<Geometry>();
		geometriesToBeMoved.add(geom);

		geometriesToBeMoved.addAll(Arrays.asList(gs));

		/*
		 * Moving the Quad for the map around and the margin Quads.
		 */

		for (Geometry g : geometriesToBeMoved) {
			// Rotate the quad such that it lies on the floor
			Quaternion quat = new Quaternion();
			quat.fromAngleAxis(1.5f * FastMath.PI, new Vector3f(1, 0, 0));
			g.setLocalRotation(quat);

			// Get the center of the mesh (no matter the original pivot)
			Vector3f center = ((Geometry) g).getMesh().getBound().getCenter().clone();
			center = quat.multLocal(center);

			// Create the node to use as pivot
			Node newPivot = new Node();
			newPivot.setLocalTranslation(center);
			newPivot.attachChild(g);

			// Reverse the pivot to match the center of the mesh
			g.setLocalTranslation(center.negate());
		}

		for (Geometry g : gs) {
			rootNode.attachChild(g);
		}

		Vector3f northSouthVector = new Vector3f(0f, 0f,
				(float) (quadDimension / 2f - (marginRatio / 2f * quadDimension)));
		Vector3f eastWestVector = new Vector3f((float) (quadDimension / 2f - (quadDimension / 2 * marginRatio)), 0, 0);
		for (Direction d : Direction.values()) {
			// Moving the Quads
			Vector3f vec = gs[d.ordinal()].getLocalTranslation();
			switch (d) {
			case East:
				vec = vec.add(eastWestVector);
				break;
			case West:
				vec = vec.subtract(eastWestVector);
				break;
			case South:
				vec = vec.add(northSouthVector);
				break;
			case North:
				vec = vec.subtract(northSouthVector);
				break;
			}
			vec = vec.add(new Vector3f(0f, 0.2f, 0f));
			gs[d.ordinal()].setLocalTranslation(vec);
		}

		/*
		 * Creating the content of the quad and loading the texture.
		 */

		Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		geom.setMaterial(mat2.clone());

		rootNode.attachChild(geom);

		/*
		 * Done with the map Quad.
		 */

		// Camera:

		float rotation = FastMath.PI;
		Quaternion initalRotation = new Quaternion();
		initalRotation.fromAngleAxis(rotation, new Vector3f(0, 4, -0.3f));
		cam.setLocation(new Vector3f(0, 10, 34));
		cam.setRotation(initalRotation);

		// cam.setLocation(new Vector3f(0, 16, 50));
		flyCam.setEnabled(true);
		flyCam.setMoveSpeed(20f);
		_ctx.setAttribute(CAMERA, cam);

		// POIs with dummy POI:
		_ctx.setAttribute(POIS, _pois);

		assetManager.registerLocator(lastFmPicUriPrefix, UrlLocator.class);
		assetManager.registerLocator(eventfulUriPrefix, UrlLocator.class);

		// HUD:
		setDisplayStatView(false);
		setDisplayFps(false);

		// Es werde Licht!
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
		rootNode.addLight(sun);

		// add arms and control here
		Quaternion yaw90 = new Quaternion();
		yaw90.fromAngleAxis(FastMath.PI / 2, new Vector3f(0, -1, 0));

		Quaternion yaw180 = new Quaternion();
		yaw180.fromAngleAxis(FastMath.PI, new Vector3f(0, -1, 0));

		Quaternion yaw_90 = new Quaternion();
		yaw_90.fromAngleAxis(FastMath.PI / 2, new Vector3f(0, 1, 0));

		int i;
		for (i = 0; i < 9; i = i + 1) {
			arms.put(i, (Node) assetManager.loadModel("Models/arm3.j3o"));
		}

		arms.get(0).setLocalTranslation(new Vector3f(0, 0.8f, 0));

		arms.get(1).setLocalTranslation(new Vector3f(8f, 0.8f, 0));

		arms.get(2).setLocalTranslation(new Vector3f(9.5f, 0.8f, 1.5f));
		arms.get(2).setLocalRotation(yaw90);

		arms.get(3).setLocalTranslation(new Vector3f(11, 0.8f, 0));

		arms.get(4).setLocalTranslation(new Vector3f(13.5f, 0.8f, 5.5f));

		arms.get(5).setLocalTranslation(new Vector3f(16.5f, 0.8f, 5.5f));

		arms.get(6).setLocalTranslation(new Vector3f(19f, 0.8f, 0));

		arms.get(7).setLocalTranslation(new Vector3f(20.5f, 0.8f, 1.5f));
		arms.get(7).setLocalRotation(yaw90);

		arms.get(8).setLocalTranslation(new Vector3f(24.5f, 0.8f, 5.5f));

		for (i = 0; i < 9; i = i + 1) {
			rootNode.attachChild(arms.get(i));
		}

		_ctx.setAttribute(ARM, arms);

		// transport belts

		for (i = 0; i < 9; i = i + 1) {
			belts.put(i, (Node) assetManager.loadModel("Models/conveyor_belt.j3o"));
		}

		belts.get(0).setLocalTranslation(new Vector3f(1, 0.75f, 0));
		belts.get(0).setLocalRotation(yaw90);

		belts.get(1).setLocalTranslation(new Vector3f(4.5f, 0.75f, 0));
		belts.get(1).setLocalRotation(yaw90);

		belts.get(2).setLocalTranslation(new Vector3f(9.5f, 0.75f, 2.5f));
		belts.get(2).setLocalRotation(yaw180);

		belts.get(4).setLocalTranslation(new Vector3f(10f, 0.75f, 5.5f));
		belts.get(4).setLocalRotation(yaw90);

		belts.get(3).setLocalTranslation(new Vector3f(12f, 0.75f, 0));
		belts.get(3).setLocalRotation(yaw90);

		belts.get(6).setLocalTranslation(new Vector3f(15.5f, 0.75f, 0));
		belts.get(6).setLocalRotation(yaw90);

		belts.get(5).setLocalTranslation(new Vector3f(17.5f, 0.75f, 5.5f));
		belts.get(5).setLocalRotation(yaw90);

		belts.get(7).setLocalTranslation(new Vector3f(20.5f, 0.75f, 2.5f));
		belts.get(7).setLocalRotation(yaw180);

		belts.get(8).setLocalTranslation(new Vector3f(21f, 0.75f, 5.5f));
		belts.get(8).setLocalRotation(yaw90);

		for (i = 0; i < 9; i = i + 1) {
			rootNode.attachChild(belts.get(i));
		}

		_ctx.setAttribute(BELT, belts);

		// pedestals

		Map<Integer, Geometry> pedestals = new Hashtable<Integer, Geometry>();

		Box b1 = new Box(0.2f, 0.4f, 0.2f);
		Material matg = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		matg.setColor("Color", ColorRGBA.Gray);

		for (i = 0; i < 9; i = i + 1) {
			pedestals.put(i, new Geometry("Box", b1));
			pedestals.get(i).setMaterial(matg);
		}

		pedestals.get(0).setLocalTranslation(new Vector3f(0, 0.4f, 0));
		pedestals.get(1).setLocalTranslation(new Vector3f(8, 0.4f, 0));
		pedestals.get(2).setLocalTranslation(new Vector3f(9.5f, 0.4f, 1.5f));
		pedestals.get(3).setLocalTranslation(new Vector3f(11, 0.4f, 0));
		pedestals.get(4).setLocalTranslation(new Vector3f(13.5f, 0.4f, 5.5f));
		pedestals.get(5).setLocalTranslation(new Vector3f(16.5f, 0.4f, 5.5f));
		pedestals.get(6).setLocalTranslation(new Vector3f(19, 0.4f, 0));
		pedestals.get(7).setLocalTranslation(new Vector3f(20.5f, 0.4f, 1.5f));
		pedestals.get(8).setLocalTranslation(new Vector3f(24.5f, 0.4f, 5.5f));

		for (i = 0; i < 9; i = i + 1) {
			rootNode.attachChild(pedestals.get(i));
		}

		// Machines

		Node furnace = (Node) assetManager.loadModel("Models/machine_black.j3o");
		machines.put(0, furnace);
		machines.get(0).setLocalRotation(yaw90);
		machines.get(0).setLocalTranslation(new Vector3f(4, 0, 0));

		Node furnace2 = (Node) assetManager.loadModel("Models/machine_black2.j3o");
		machines.put(1, furnace2);
		machines.get(1).setLocalTranslation(new Vector3f(9.5f, 0, 5.5f));
		machines.get(1).setLocalRotation(yaw_90);

		Node production = (Node) assetManager.loadModel("Models/machine_white.j3o");
		machines.put(2, production);
		machines.get(2).setLocalRotation(yaw90);
		machines.get(2).setLocalTranslation(new Vector3f(15, 0, 0));

		Node production2 = (Node) assetManager.loadModel("Models/machine_white2.j3o");
		machines.put(3, production2);
		machines.get(3).setLocalRotation(yaw90);
		machines.get(3).setLocalTranslation(new Vector3f(20.5f, 0, 5.5f));

		for (i = 0; i < 4; i = i + 1) {
			rootNode.attachChild(machines.get(i));
		}

		_ctx.setAttribute(MACHINES, machines);

		for (i = 0; i < 4; i = i + 1) {
			mstates.put(i, "off");
			maction.put(i, "done");
		}

		_ctx.setAttribute(MSTATES, mstates);
		_ctx.setAttribute(MACTION, maction);

		// storage
		Map<Integer, Node> storage = new Hashtable<Integer, Node>();

		for (i = 0; i < 5; i = i + 1) {
			storage.put(i, (Node) assetManager.loadModel("Models/storage.j3o"));
		}

		storage.get(0).setLocalTranslation(new Vector3f(-1.5f, 0, 0));
		storage.get(1).setLocalTranslation(new Vector3f(9.5f, 0, 0));
		storage.get(2).setLocalTranslation(new Vector3f(15, 0, 5.5f));
		storage.get(3).setLocalTranslation(new Vector3f(20.5f, 0, 0));
		storage.get(4).setLocalTranslation(new Vector3f(26, 0, 5.5f));

		for (i = 0; i < 5; i = i + 1) {
			rootNode.attachChild(storage.get(i));
		}

		_ctx.setAttribute(STORAGE, storage);

		for (i = 0; i < 5; i = i + 1) {
			storageCount.put(i, 0);
		}

		_ctx.setAttribute(STORAGECOUNT, storageCount);
		
		for (i = 0; i < 5; i = i + 1) {
			sstates.put(i, "free");
		}
		_ctx.setAttribute(SSTATES, sstates);
		

		for (i = 0; i < 5; i = i + 1) {
			saction.put(i, "done");
		}
		_ctx.setAttribute(SACTION, saction);

		// add products

		Map<Integer, Node> iron_ore = new Hashtable<Integer, Node>();
		for (i = 0; i < 6; i = i + 1) {
			iron_ore.put(i, (Node) assetManager.loadModel("Models/iron_ore.j3o"));
		}
		iron_ore.get(0).setLocalTranslation(new Vector3f(-1.5f, 0.25f, 0));
		rootNode.attachChild(iron_ore.get(0));
		iron_ore.get(1).setLocalTranslation(new Vector3f(-1.5f, 0.5f, 0));
		rootNode.attachChild(iron_ore.get(1));
		iron_ore.get(2).setLocalTranslation(new Vector3f(-1.5f, 0.75f, 0));
		rootNode.attachChild(iron_ore.get(2));
		storageCount.put(0, 3);

		Map<Integer, Node> iron_plate = new Hashtable<Integer, Node>();
		for (i = 0; i < 12; i = i + 1) {
			iron_plate.put(i, (Node) assetManager.loadModel("Models/iron_plate.j3o"));
		}
		Map<Integer, Node> steel_plate = new Hashtable<Integer, Node>();
		for (i = 0; i < 9; i = i + 1) {
			steel_plate.put(i, (Node) assetManager.loadModel("Models/steel_plate.j3o"));
		}
		Map<Integer, Node> gear_wheel = new Hashtable<Integer, Node>();
		for (i = 0; i < 9; i = i + 1) {
			gear_wheel.put(i, (Node) assetManager.loadModel("Models/gear_wheel.j3o"));
		}
		Map<Integer, Node> flamethrower = new Hashtable<Integer, Node>();
		for (i = 0; i < 6; i = i + 1) {
			flamethrower.put(i, (Node) assetManager.loadModel("Models/flamethrower.j3o"));
		}

		products.put(0, iron_ore);
		products.put(1, iron_plate);
		products.put(2, steel_plate);
		products.put(3, gear_wheel);
		products.put(4, flamethrower);

		// arm node products
		for (i = 0; i < 5; i = i + 1) {
			products.get(i).get(3).setLocalTranslation(new Vector3f(0, 0.25f, 0));
		}
		for (i = 1; i < 4; i = i + 1) {
			products.get(i).get(6).setLocalTranslation(new Vector3f(0, 0.25f, 0));
		}

		products.get(1).get(9).setLocalTranslation(new Vector3f(0, 0.25f, 0));

		// storage products
		for (i = 0; i < 3; i = i + 1) {
			Float numb = i * 0.25f;
			products.get(1).get(i).setLocalTranslation(new Vector3f(9.5f, numb, 0));
		}
		for (i = 0; i < 3; i = i + 1) {
			Float numb = i * 0.25f;
			products.get(2).get(i).setLocalTranslation(new Vector3f(15, numb, 5.5f));
		}
		for (i = 0; i < 3; i = i + 1) {
			Float numb = i * 0.25f;
			products.get(3).get(i).setLocalTranslation(new Vector3f(20.5f, numb, 0));
		}
		for (i = 0; i < 3; i = i + 1) {
			Float numb = i * 0.25f;
			products.get(4).get(i).setLocalTranslation(new Vector3f(26, numb, 5.5f));
		}

		// belt products
		Vector3f scal = new Vector3f(2, 2, 2);
		Vector3f transl = new Vector3f(-0.5f, 0, 0.25f);
		for (i = 1; i < 4; i = i + 1) {
			products.get(i).get(7).setLocalScale(scal);
			products.get(i).get(7).setLocalTranslation(transl);
		}

		for (i = 0; i < 5; i = i + 1) {
			products.get(i).get(4).setLocalScale(scal);
			products.get(i).get(4).setLocalTranslation(transl);
		}

		products.get(1).get(10).setLocalScale(scal);
		products.get(1).get(10).setLocalTranslation(transl);

		_ctx.setAttribute(PRODUCTS, products);
		//

		_ctx.setAttribute(ARMUP, _armupdate);
		_ctx.setAttribute(MACHINEUP, _machineupdate);
		_ctx.setAttribute(STORAGEUP, _storageupdate);

		// arm animation

		for (i = 0; i < 9; i = i + 1) {
			anims.put(i, "");
		}

		_ctx.setAttribute(ANIMS, anims);

		// belt animation

		Map<Integer, String> banims = new Hashtable<Integer, String>();

		for (i = 0; i < 9; i = i + 1) {
			banims.put(i, "");
		}

		_ctx.setAttribute(BANIMS, banims);

		// arm states
		Map<Integer, String> states = new Hashtable<Integer, String>();

		for (i = 0; i < 9; i = i + 1) {
			states.put(i, "");
		}
		_ctx.setAttribute(STATES, states);

		// belt states
		Map<Integer, String> bstates = new Hashtable<Integer, String>();

		for (i = 0; i < 9; i = i + 1) {
			bstates.put(i, "");
		}
		_ctx.setAttribute(BSTATES, bstates);

		// arm control
		Map<Integer, AnimControl> contr = new Hashtable<Integer, AnimControl>();

		Map<Integer, AnimChannel> chann = new Hashtable<Integer, AnimChannel>();

		for (i = 0; i < 9; i = i + 1) {
			contr.put(i, arms.get(i).getChild("Rmk3.010").getControl(AnimControl.class));
			contr.get(i).addListener(this);
			chann.put(i, contr.get(i).createChannel());
		}

		_ctx.setAttribute(CONTROL, contr);
		_ctx.setAttribute(CHANNEL, chann);

		// add skeletonControl

		for (i = 0; i < 9; i = i + 1) {
			skelContr.put(i, arms.get(i).getChild("Rmk3.010").getControl(SkeletonControl.class));
		}

		_ctx.setAttribute(SKELCONTROL, skelContr);

		// belt control

		Map<Integer, AnimControl> bcontr = new Hashtable<Integer, AnimControl>();

		Map<Integer, AnimChannel> bchann = new Hashtable<Integer, AnimChannel>();

		for (i = 0; i < 9; i = i + 1) {
			bcontr.put(i, belts.get(i).getChild("Armature").getControl(AnimControl.class));
			bcontr.get(i).addListener(this);
			bchann.put(i, bcontr.get(i).createChannel());
		}

		_ctx.setAttribute(BCONTROL, bcontr);
		_ctx.setAttribute(BCHANNEL, bchann);

		// animations
		for (i = 0; i < 9; i = i + 1) {
			bchann.get(i).setAnim("forward");
			bchann.get(i).setLoopMode(LoopMode.DontLoop);
		}

		for (i = 0; i < 9; i = i + 1) {
			chann.get(i).setAnim("down_left");
			chann.get(i).setLoopMode(LoopMode.DontLoop);
		}

	}

	/**
	 * The update cycle of jMonkey's scene graph.
	 *
	 */
	public void simpleUpdate(float tpf) {
		com.jme3.scene.Node product = products.get(0).get(3);
		for (Integer id : _armupdate.keySet()) {
			//If the arm update map has grip saved attach the correct item to the robot arm
			if (_armupdate.get(id).equals("grip")) {
				com.jme3.scene.Node head = (com.jme3.scene.Node) skelContr.get(id).getAttachmentsNode("Bone.004");
				switch (id) {
				case 0:
					//put the state of the robot arm to busy
					sstates.put(0, "busy");
					//the storage count of the previous storage box is subtracted by one, since the robot now grips the item
					storageCount.put(0, storageCount.get(0) - 1);
					//remove the visible item from the parent (rootNode) to not see it anymore
					products.get(0).get(storageCount.get(0)).removeFromParent();
					//save the item that is normaly attached to the robot arm in product, which is attached to the robot arm at the end of our switch case
					product = products.get(0).get(3);
					break;

				case 1:
					products.get(1).get(5).removeFromParent();
					product = products.get(1).get(3);
					break;
				case 2:
					sstates.put(1, "busy");
					products.get(1).get(1).removeFromParent();
					product = products.get(1).get(9);
					break;
				case 3:
					sstates.put(1, "busy");
					products.get(1).get(2).removeFromParent();
					product = products.get(1).get(6);
					break;

				case 4:
					products.get(2).get(5).removeFromParent();
					product = products.get(2).get(3);
					break;
				case 5:
					sstates.put(2, "busy");
					storageCount.put(2, storageCount.get(2) - 1);
					products.get(2).get(storageCount.get(2)).removeFromParent();
					product = products.get(2).get(6);
					break;

				case 6:
					products.get(3).get(5).removeFromParent();
					product = products.get(3).get(3);
					break;
				case 7:
					sstates.put(3, "busy");
					storageCount.put(3, storageCount.get(3) - 1);
					products.get(3).get(storageCount.get(3)).removeFromParent();
					product = products.get(3).get(6);
					break;
				case 8:
					products.get(4).get(5).removeFromParent();
					product = products.get(4).get(3);
					break;
				}
				//attach the item to the head of the corresponding robot arm to make it visible
				head.attachChild(product);
				//remove the action from the map, so that it isn't done again
				_armupdate.remove(id);
				//set the states of the robot arms to free, since they are no longer doing an action
				switch (id) {
				case 1:
					sstates.put(1, "free");
					break;
				case 4:
					sstates.put(2, "free");
					break;
				case 6:
					sstates.put(3, "free");
					break;
				case 8:
					sstates.put(4, "free");
					break;
				}
				//set the action indicator to done, so that another action may be posted to the robot arm
				anims.put(id, "done");
			}

			else if (_armupdate.get(id).equals("drop")) {
				com.jme3.scene.Node belt = (com.jme3.scene.Node) belts.get(id).getChild("clamp.003");
				Integer numb;
				switch (id) {
				case 0:
					products.get(0).get(3).removeFromParent();
					belt.attachChild(products.get(0).get(4));
					break;
				//Here the hack for storage count 1 mentioned in the GET arm/id/state
				case 1:
					sstates.put(1, "busy");
					products.get(1).get(3).removeFromParent();
					//If the storage count is 0, this means that there has been never been iron plate placed into storage 1. 
					//Since one of the robot arms, that takes something out of the storage box 1, only reacts to iron plate 1 and the other only to iron plate 2, iron plate 0 is never attached to root node, but rather iron plate 1.
					if (storageCount.get(1) == 0) {
						numb = 1;
					} else {
						numb = storageCount.get(1);
					}

					rootNode.attachChild(products.get(1).get(numb));
					//if the storage count was previously 1, then change it to 2, since iron plate 2 should be attached next, for the other robot arm to sense something in the storage box
					if (numb == 1) {
						storageCount.put(1, 2);
					//if the storage count was not previously 1, then change it to one, since the next time, the robot arm accessing iron plate 1 should get the chance.
					} else {
						storageCount.put(1, 1);
					}
					break;
				case 2:
					products.get(1).get(9).removeFromParent();
					belt.attachChild(products.get(1).get(10));
					break;
				case 3:
					products.get(1).get(6).removeFromParent();
					belt.attachChild(products.get(1).get(7));
					break;
				case 4:
					sstates.put(2, "busy");
					products.get(2).get(3).removeFromParent();
					numb = storageCount.get(2);
					rootNode.attachChild(products.get(2).get(numb));
					storageCount.put(2, numb + 1);
					break;
				case 5:
					products.get(2).get(6).removeFromParent();
					belt.attachChild(products.get(2).get(7));
					break;
				case 6:
					sstates.put(3, "busy");
					products.get(3).get(3).removeFromParent();
					numb = storageCount.get(3);
					rootNode.attachChild(products.get(3).get(numb));
					storageCount.put(3, numb + 1);
					break;
				case 7:
					products.get(3).get(6).removeFromParent();
					belt.attachChild(products.get(3).get(7));
					break;
				case 8:
					sstates.put(4, "busy");
					products.get(4).get(3).removeFromParent();
					numb = storageCount.get(4);
					rootNode.attachChild(products.get(4).get(numb));
					storageCount.put(4, numb + 1);
					break;

				}
				_armupdate.remove(id);
				switch (id) {
				case 0:
					sstates.put(0, "free");
					break;
				case 2:
				case 3:
					sstates.put(1, "free");
					break;
				case 5:
					sstates.put(2, "free");
					break;
				case 7:
					sstates.put(3, "free");
					break;
				}
				anims.put(id, "done");

			}
		}

		for (Integer id : _machineupdate.keySet()) {
			com.jme3.scene.Node belt = (com.jme3.scene.Node) belts.get(id).getChild("clamp.003");
			product = products.get(0).get(5);
			switch (id) {
			case 0:
				product.removeFromParent();
				belt = (com.jme3.scene.Node) belts.get(1).getChild("clamp.003");
				product = products.get(1).get(4);
				break;

			case 1:
				products.get(1).get(11).removeFromParent();
				belt = (com.jme3.scene.Node) belts.get(4).getChild("clamp.003");
				product = products.get(2).get(4);
				break;
			case 2:
				products.get(1).get(8).removeFromParent();
				belt = (com.jme3.scene.Node) belts.get(6).getChild("clamp.003");
				product = products.get(3).get(4);
				break;
			case 3:
				products.get(2).get(8).removeFromParent();
				products.get(3).get(8).removeFromParent();
				belt = (com.jme3.scene.Node) belts.get(8).getChild("clamp.003");
				product = products.get(4).get(4);
				break;
			}
			belt.attachChild(product);
			_machineupdate.remove(id);
			mstates.put(id, "off");
			maction.put(id, "done");
		}
		for (Integer id : _storageupdate.keySet()) {
			if (_storageupdate.get(id).equals("fill")){
				rootNode.attachChild(products.get(id).get(0));
				rootNode.attachChild(products.get(id).get(1));
				rootNode.attachChild(products.get(id).get(2));
				storageCount.put(id, 3);
			}
			else {
				if (storageCount.get(id).equals(3)){
					products.get(id).get(0).removeFromParent();
					products.get(id).get(1).removeFromParent();
					products.get(id).get(2).removeFromParent();
				}
				else if (storageCount.get(id).equals(2)){
					products.get(id).get(0).removeFromParent();
					products.get(id).get(1).removeFromParent();
				}
				else if (storageCount.get(id).equals(1)){
					products.get(id).get(0).removeFromParent();
				}
				else {
					
				}
				
			}
			_storageupdate.remove(id);
			saction.put(id, "done");
		}

	}

	@Override
	public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
		Map<Integer, String> test = (Hashtable<Integer, String>) _ctx.getAttribute(ANIMS);
		Map<Integer, String> states = (Hashtable<Integer, String>) _ctx.getAttribute(STATES);
		Map<Integer, AnimChannel> chann = (Hashtable<Integer, AnimChannel>) _ctx.getAttribute(CHANNEL);

		Map<Integer, String> banims = (Hashtable<Integer, String>) _ctx.getAttribute(BANIMS);
		Map<Integer, String> bstates = (Hashtable<Integer, String>) _ctx.getAttribute(BSTATES);
		Map<Integer, AnimChannel> bchanns = (Hashtable<Integer, AnimChannel>) _ctx.getAttribute(BCHANNEL);

		Map<Integer, Map<Integer, com.jme3.scene.Node>> products = (Hashtable<Integer, Map<Integer, com.jme3.scene.Node>>) _ctx
				.getAttribute(ContextListenerApp.PRODUCTS);
		com.jme3.scene.Node product;
		com.jme3.scene.Node product1;

		for (int t : chann.keySet()) {
			if (chann.get(t).equals(channel)) {
				if (animName.equals("down")) {
					states.put(t, "down_right");
				} else if (animName.equals("Rotate_180")) {
					states.put(t, "up_right");
				} else if (animName.equals("Rotate180")) {
					states.put(t, "up_left");
				} else if (animName.equals("up")) {
					states.put(t, "up_right");
				} else {
					states.put(t, animName);
				}
				test.put(t, "done");
			}
		}

		for (int t : bchanns.keySet()) {
			if (bchanns.get(t).equals(channel)) {
				if (animName.equals("forward2")) {

					bchanns.get(t).setAnim("still");
					bchanns.get(t).setLoopMode(LoopMode.DontLoop);
					if (t == 0 || t == 1) {
						product = products.get(t).get(4);
						Vector3f pos = product.getWorldTranslation();
						product.removeFromParent();
						product1 = products.get(t).get(5);
						product1.setLocalTranslation(pos);
						rootNode.attachChild(product1);
					} else if (t == 2) {
						product = products.get(1).get(10);
						Vector3f pos = product.getWorldTranslation();
						product.removeFromParent();
						product1 = products.get(1).get(11);
						product1.setLocalTranslation(pos);
						rootNode.attachChild(product1);
					} else if (t == 3) {
						product = products.get(1).get(7);
						Vector3f pos = product.getWorldTranslation();
						product.removeFromParent();
						product1 = products.get(1).get(8);
						product1.setLocalTranslation(pos);
						rootNode.attachChild(product1);
					} else if (t == 4) {
						product = products.get(2).get(4);
						Vector3f pos = product.getWorldTranslation();
						product.removeFromParent();
						product1 = products.get(2).get(5);
						product1.setLocalTranslation(pos);
						rootNode.attachChild(product1);
					} else if (t == 5) {
						product = products.get(2).get(7);
						Vector3f pos = product.getWorldTranslation();
						product.removeFromParent();
						product1 = products.get(2).get(8);
						product1.setLocalTranslation(pos);
						rootNode.attachChild(product1);
					} else if (t == 6) {
						product = products.get(3).get(4);
						Vector3f pos = product.getWorldTranslation();
						product.removeFromParent();
						product1 = products.get(3).get(5);
						product1.setLocalTranslation(pos);
						rootNode.attachChild(product1);
					} else if (t == 7) {
						product = products.get(3).get(7);
						Vector3f pos = product.getWorldTranslation();
						product.removeFromParent();
						product1 = products.get(3).get(8);
						product1.setLocalTranslation(pos);
						rootNode.attachChild(product1);
					} else {
						product = products.get(4).get(4);
						Vector3f pos = product.getWorldTranslation();
						product.removeFromParent();
						product1 = products.get(4).get(5);
						product1.setLocalTranslation(pos);
						rootNode.attachChild(product1);
					}

				}
				banims.put(t, "done");
				bstates.put(t, "done");

			}
		}

	}

	@Override
	public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
		Map<Integer, String> test = (Hashtable<Integer, String>) _ctx.getAttribute(ANIMS);
		Map<Integer, String> states = (Hashtable<Integer, String>) _ctx.getAttribute(STATES);
		Map<Integer, AnimChannel> chann = (Hashtable<Integer, AnimChannel>) _ctx.getAttribute(CHANNEL);

		Map<Integer, String> banims = (Hashtable<Integer, String>) _ctx.getAttribute(BANIMS);
		Map<Integer, String> bstates = (Hashtable<Integer, String>) _ctx.getAttribute(BSTATES);
		Map<Integer, AnimChannel> bchanns = (Hashtable<Integer, AnimChannel>) _ctx.getAttribute(BCHANNEL);

		for (int t : chann.keySet()) {
			if (chann.get(t).equals(channel)) {
				test.put(t, "moving");
				states.put(t, "moving");
			}

		}

		for (int s : bchanns.keySet()) {
			if (bchanns.get(s).equals(channel)) {
				banims.put(s, "moving");
				bstates.put(s, "moving");
			}

		}

	}

	private ActionListener actionListener = new ActionListener() {
		@Override
		public void onAction(String name, boolean keyPressed, float tpf) {

		}
	};

}
