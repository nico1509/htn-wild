package edu.kit.aifb.ls3.kaefer.jmonkeyld;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.FOAF;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.namespace.XSD;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

@Path("/")
public class RESTinterface {

	static Resource geovocFeature = new Resource("<http://geovocab.org/spatial#Feature>", true);

	static final class SCENE {
		private static final String namespace_scene = "http://vocab.arvida.de/2014/03/scenegraph/vocab#";

		static final Resource SceneNode = new Resource("<" + namespace_scene + "SceneNode" + ">", true);
		static final Resource nodeFixedCoordinateSystem = new Resource(
				"<" + namespace_scene + "nodeFixedCoordinateSystem" + ">", true);
		static final Resource DefinedSpatialRelationship = new Resource(
				"<" + namespace_scene + "DefinedSpatialRelationship" + ">", true);
	}

	static final class MATHS {
		private static final String namespace_maths = "http://vocab.arvida.de/2014/03/maths/vocab#";

		static final Resource CartesianCoordinateSystem = new Resource(
				"<" + namespace_maths + "CartesianCoordinateSystem" + ">", true);
		static final Resource Vector3D = new Resource("<" + namespace_maths + "Vector3D" + ">", true);
		static final Resource RhCartesianCoordinateSystem3D = new Resource(
				"<" + namespace_maths + "RightHandedCartesianCoordinateSystem3D" + ">", true); // was lowercase
		static final Resource x = new Resource("<" + namespace_maths + "x" + ">", true);
		static final Resource y = new Resource("<" + namespace_maths + "y" + ">", true);
		static final Resource z = new Resource("<" + namespace_maths + "z" + ">", true);
	}

	static final class SPATIAL {
		private static final String namespace_spatial = "http://vocab.arvida.de/2014/03/spatial/vocab#";

		static final Resource translation = new Resource("<" + namespace_spatial + "translation" + ">", true);
		static final Resource Translation3D = new Resource("<" + namespace_spatial + "Translation3D" + ">", true);
		static final Resource sourceCoordinateSystem = new Resource(
				"<" + namespace_spatial + "sourceCoordinateSystem" + ">", true);
		static final Resource targetCoordinateSystem = new Resource(
				"<" + namespace_spatial + "targetCoordinateSystem" + ">", true);
	}

	static final class VOM {
		private static final String namespace_vom = "http://vocab.arvida.de/2014/03/vom/vocab#";

		static final Resource quantityValue = new Resource("<" + namespace_vom + "quantityValue" + ">", true);
	}

	static final class UKEAQ {
		private static final String voc = "http://www.student.kit.edu/~ukeaq/uni/voc.ttl#";

		static final Resource hasStatus = new Resource("<" + voc + "hasStatus" + ">", true);
		static final Resource hasAction = new Resource("<" + voc + "hasAction" + ">", true);
		static final Resource hasCurrentAction = new Resource("<" + voc + "hasCurrentAction" + ">", true);
		static final Resource hasCurrentState = new Resource("<" + voc + "hasCurrentState" + ">", true);
		static final Resource grip = new Resource("<" + voc + "grip" + ">", true);
		static final Resource up_left = new Resource("<" + voc + "up_left" + ">", true);
		static final Resource move_up_left = new Resource("<" + voc + "move_up_left" + ">", true);
		static final Resource rotate_180 = new Resource("<" + voc + "Rotate_180" + ">", true);
		static final Resource down_right = new Resource("<" + voc + "down_right" + ">", true);
		static final Resource move_down_right = new Resource("<" + voc + "move_down_right" + ">", true);
		static final Resource drop = new Resource("<" + voc + "drop" + ">", true);
		static final Resource down_left = new Resource("<" + voc + "down_left" + ">", true);
		static final Resource move_down_left = new Resource("<" + voc + "move_down_left" + ">", true);
		static final Resource up_right = new Resource("<" + voc + "up_right" + ">", true);
		static final Resource move_up_right = new Resource("<" + voc + "move_up_right" + ">", true);
		static final Resource open = new Resource("<" + voc + "open" + ">", true);
		static final Resource closed = new Resource("<" + voc + "closed" + ">", true);
		static final Resource full = new Resource("<" + voc + "full" + ">", true);
		static final Resource empty = new Resource("<" + voc + "empty" + ">", true);
		static final Resource hasGripperState = new Resource("<" + voc + "hasGripperState" + ">", true);
		static final Resource hasCapacity = new Resource("<" + voc + "hasCapacity" + ">", true);
		static final Resource invokeAction = new Resource("<" + voc + "invokeAction" + ">", true);
		static final Resource on = new Resource("<" + voc + "on" + ">", true);
		static final Resource off = new Resource("<" + voc + "off" + ">", true);
		static final Resource on_back = new Resource("<" + voc + "on_back" + ">", true);
		static final Resource hasSensorInState = new Resource("<" + voc + "hasSensorInState" + ">", true);
		static final Resource hasSensorOutState = new Resource("<" + voc + "hasSensorOutState" + ">", true);
		static final Resource machine = new Resource("<" + voc + "machine" + ">", true);
		static final Resource storage = new Resource("<" + voc + "storage" + ">", true);
		static final Resource fill = new Resource("<" + voc + "fill" + ">", true);

	}

	@Context
	ServletContext ctx;

	@GET
	public Response overview() {

		return null;
	}

	@GET
	@Path("arm/{id}")
	public Response arm(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Resource doc = new Resource("<" + uInfo.getAbsolutePath().toString() + ">", true);
		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#arm>", true);
		Resource selfLoc = new Resource("<" + uInfo.getAbsolutePath().toString() + "#loc>", true);
		Resource selfLocData = new Resource(
				"<" + uInfo.getAbsolutePath().resolve(id + "/location-data#data").toString() + ">", true);
		Resource selfCoordinateSystem = new Resource("<" + uInfo.getAbsolutePath().toString() + "#selfCS>", true);
		Resource self2worldSpatialRelation = new Resource("<" + uInfo.getAbsolutePath().toString() + "#selfSR>", true);
		Resource worldCoordinateSystem = new Resource(
				"<" + uInfo.getAbsolutePath().resolve("/worldCoordinateSystem#worldCS").toString() + ">", true);
		Resource action = new Resource("<" + uInfo.getAbsolutePath().resolve(id + "/action#action").toString() + ">",
				true);
		Resource state = new Resource("<" + uInfo.getAbsolutePath().resolve(id + "/state#state").toString() + ">",
				true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { doc, FOAF.PRIMARYTOPIC, self },
				new Node[] { self, RDF.TYPE, SCENE.SceneNode },
				new Node[] { self, SCENE.nodeFixedCoordinateSystem, selfCoordinateSystem },
				new Node[] { selfCoordinateSystem, RDF.TYPE, MATHS.CartesianCoordinateSystem },
//			new Node[] { worldCoordinateSystem, RDF.TYPE, MATHS.RhCartesianCoordinateSystem3D },
				new Node[] { self2worldSpatialRelation, RDF.TYPE, SCENE.DefinedSpatialRelationship },
				new Node[] { self2worldSpatialRelation, SPATIAL.sourceCoordinateSystem, worldCoordinateSystem },
				new Node[] { self2worldSpatialRelation, SPATIAL.targetCoordinateSystem, selfCoordinateSystem },
				new Node[] { self2worldSpatialRelation, SPATIAL.translation, selfLoc },
				new Node[] { selfLoc, RDF.TYPE, SPATIAL.Translation3D },
				new Node[] { selfLoc, VOM.quantityValue, selfLocData },
				new Node[] { self, UKEAQ.hasCurrentAction, action }, new Node[] { self, UKEAQ.hasCurrentState, state }

		});

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();
	}

	/*
	 * ============================== ARM
	 */
	@GET
	@Path("arm/{id}/location-data")
	public Response armlocation(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, com.jme3.scene.Node> arm = (Hashtable<Integer, com.jme3.scene.Node>) ctx
				.getAttribute(ContextListenerApp.ARM);
		if (arm == null)
			return Response.status(404).build();

		Vector3f vec = ((Spatial) arm.get(id)).getWorldTranslation();

		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#data>", true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { self, RDF.TYPE, MATHS.Vector3D },
				new Node[] { self, MATHS.x, new Literal(Float.toString(vec.getX()), XSD.FLOAT) },
				new Node[] { self, MATHS.y, new Literal(Float.toString(vec.getY()), XSD.FLOAT) },
				new Node[] { self, MATHS.z, new Literal(Float.toString(vec.getZ()), XSD.FLOAT) } });

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();
	}

	@GET
	@Path("arm/{id}/action")
	public Response armactionG(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, String> anims = (Hashtable<Integer, String>) ctx.getAttribute(ContextListenerApp.ANIMS);
		if (anims == null)
			return Response.status(404).build();

		String action = anims.get(id);

		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#action>", true);
		// Resource actionstatus = new Resource("<" + uInfo.getAbsolutePath().toString()
		// + "#"+ action + ">", true);
		Resource actionstatus = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + action + ">", true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { self, UKEAQ.hasStatus, actionstatus }, });

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();

	}

	@POST
	@Path("arm/{id}/action")
	public Response armactionP(Iterable<Node[]> nxp, @PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, AnimChannel> channel = (Hashtable<Integer, AnimChannel>) ctx
				.getAttribute(ContextListenerApp.CHANNEL);
		if (channel == null)
			return Response.status(404).build();

		Map<Integer, String> anims = (Hashtable<Integer, String>) ctx.getAttribute(ContextListenerApp.ANIMS);

		Map<Integer, String> armup = (ConcurrentHashMap<Integer, String>) ctx.getAttribute(ContextListenerApp.ARMUP);

		for (Node[] nx : nxp) {
			if (nx[1].equals(UKEAQ.invokeAction)) {
				//The grip action of a robot arm is not an animation but rather a reassignment of the gripped object from a previous node to the node of the robot arm.
				//The reassignment is handled in the simpleUdpate of the ContextListenerApp, since rootNode is often the previous node and a threading error occurs, if not handled in the simpleUpdate.
				if (nx[2].equals(UKEAQ.grip)) {
					//set the action indicator to moving, so that no other action is posted
					anims.put(id, "moving");
					//put the grip action into the arm update map, which is then handled in the simpleUpdate, since it needs to access rootNode
					armup.put(id, "grip");

				//The drop action of a robot arm is not an animation but rather a reassignment of the to be dropped object from the robot arm node to a succeeding node.
				} else if (nx[2].equals(UKEAQ.drop)) {
					anims.put(id, "moving");
					armup.put(id, "drop");
				}

				//Here the animation of the 3D robot arm, which we want to trigger, to move down right has as name "down" but is named as "down_right" in the vocabulary for clarity
				else if (nx[2].equals(UKEAQ.down_right)) {
					channel.get(id).setAnim("down");
					channel.get(id).setLoopMode(LoopMode.DontLoop);
				//Here the animation of the 3D robot arm, which we want to trigger, to move up right has as name "up" but is named as "up_right" in the vocabulary for clarity
				} else if (nx[2].equals(UKEAQ.up_right)) {
					channel.get(id).setAnim("up");
					channel.get(id).setLoopMode(LoopMode.DontLoop);
				//All other animation names correspond to the vocabulary names
				} else {
					String s = nx[2].getLabel();
					String s1 = s.substring(s.indexOf("#") + 1);
					channel.get(id).setAnim(s1);
					channel.get(id).setLoopMode(LoopMode.DontLoop);
				}

			}

		}

		return Response.noContent().build();

	}

	@GET
	@Path("arm/{id}/state")
	public Response armstateG(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, String> states = (Hashtable<Integer, String>) ctx.getAttribute(ContextListenerApp.STATES);

		Map<Integer, SkeletonControl> skeletonControl = (Map<Integer, SkeletonControl>) ctx
				.getAttribute(ContextListenerApp.SKELCONTROL);
		com.jme3.scene.Node head = (com.jme3.scene.Node) skeletonControl.get(id).getAttachmentsNode("Bone.004");

		Map<Integer, Map<Integer, com.jme3.scene.Node>> products = (Hashtable<Integer, Map<Integer, com.jme3.scene.Node>>) ctx
				.getAttribute(ContextListenerApp.PRODUCTS);
		Map<Integer, Integer> storagec = (Hashtable<Integer, Integer>) ctx
				.getAttribute(ContextListenerApp.STORAGECOUNT);
		Map<Integer, com.jme3.scene.Node> belts = (Hashtable<Integer, com.jme3.scene.Node>) ctx
				.getAttribute(ContextListenerApp.BELT);
		com.jme3.scene.Node belt = (com.jme3.scene.Node) belts.get(id).getChild("clamp.003");
		com.jme3.scene.Node rootNode = (com.jme3.scene.Node) ctx.getAttribute(ContextListenerApp.ROOTNODE);

		String gripperState = "open";
		String capacity = "empty";
		String sensor_in = "empty";
		String sensor_out = "empty";

		//Here the cases are for each single arm. arm/0 is handled in case 0, arm/1 is handled in case 1, etc.
		switch (id) {
		case 0:
			//robot arm 0 only interacts with iron ore, which can be accessed through products.get(0)
			//There are in total 6 iron ore objects, which are either visible or not visible (attached to a node or not), which are accessed through products.get(0).get(i) with i=0..5
			//The storage box 0 has three iron ore objects. Iron ore 0 is always at the bottom, iron ore 1 is the second object in the storage box and iron ore 2 is the third object.
			//Iron ore 3 is always the object attached to the robot arm node. 
			//Iron ore 4 is always the object attached to the node of the succeeding transport belt 0
			//Iron ore 5 is always the object which takes the visible place of iron ore 4, once it has reached the end of the belt and is deleted. 
			//If the node of the robot arm (head) has iron ore 3 attached, this means, that the gripper has something in its grip, is therefore closed and the robot arm has no capacity left to grip something else.
			if (head.hasChild(products.get(0).get(3))) {
				gripperState = "closed";
				capacity = "full";
			} else {
				gripperState = "open";
				capacity = "empty";
			}
			//If rootNode (the visible scene) has iron ore 0 attached, this means, that there is at least one iron ore in the storage box.
			//At least one iron ore in the storage box means, that there is an object to grip and therefore the input sensor of the robot arm indicates, that there is something there by saying, that it is full.
			//Else it means, that there are no objects in the storage box and therefore the input sensor is empty (senses nothing)
			if (rootNode.hasChild(products.get(0).get(0))) {
				sensor_in = "full";
			} else {
				sensor_in = "empty";
			}

			//If the conveyor belt node has iron ore 4 attached, this means, that there is an object at the robot arm's output location and therefore, the output sensor indicates, that the location is full. 
			if (belt.hasChild(products.get(0).get(4))) {
				sensor_out = "full";
			} else {
				sensor_out = "empty";
			}
			break;
		case 1:
			//robot arm 1 only interacts with iron plate, which can be accessed through products.get(1)
			//Iron plate 3 is always the object attached to robot arm 1
			//iron plate 4 and 5 are always the ones attached to the incoming transport belt 1
			//iron plate 0,1,2 are always attached to the storage box 1 at the output location of robot arm 1
			//all storage boxes always have the items 0,1,2 to be attached
			//robots 0, 1, 4, 6 and 8 always have item 3 of the corresponding product attached
			//robot 2 always has iron plate 9 attached
			//robots 3, 5 and 7 always have item 6 of the corresponding product attached
			//transport belts always have an item for their input location and their output location.
			
			if (head.hasChild(products.get(1).get(3))) {
				gripperState = "closed";
				capacity = "full";
			} else {
				gripperState = "open";
				capacity = "empty";
			}

			//Here it is checked, whether the storage box is too full to place something in it or not
			//Storagec indicates the capacity of a storage box, where the capacity of storage box 1 is accessed through storagec.get(1)
			//A little hack here: later two robot arms want to access the storage box to take out iron plate. In order for them not to clash, one may only access iron ore 2, while the other may only access iron ore 1.
			//Other part is found in simpleUpdate at the else statement of _armupdate.get(id).equals("drop"), case 1.
			//If there is no object in the storage box, then the output sensor of the arm is empty and it may drop something into the storage box
			if (storagec.get(1) == 0) {
				sensor_out = "empty";
			//If rootnode has iron plate 2 and iron plate 1 attached as child (visible), then the storage box is full and the robot arm may place nothing in the storage box
			} else if (rootNode.hasChild(products.get(1).get(2)) && rootNode.hasChild(products.get(1).get(1))) {
				sensor_out = "full";
			//Otherwise, if the storage count is one and iron plate 1 is attached to rootnode, this means, that there is still room for a second iron plate in the storage box
			//Since there is room for a second iron plate in the storage box, the storage c is changed to two, so that the next incoming iron plate is place into the iron plate 2 slot
			//Here storagec is an indicator on where to place the iron plate in the storage box instead of counting how many items are in the storage box
			//The item count is not important here, since storage box 1 can only have two items in it and each can only be gripped by one explicit robot arm
			//Robot arm 2 only ever accesses iron plate 1 and robot arm 3 only ever accesses iron plate 2
			} else if (storagec.get(1) == 1 && rootNode.hasChild(products.get(1).get(1))) {
				storagec.put(1, 2);
				sensor_out = "empty";
			} else if (storagec.get(1) == 2 && rootNode.hasChild(products.get(1).get(2))) {
				storagec.put(1, 1);
				sensor_out = "empty";
			} else {
				sensor_out = "empty";
			}

			//if the incoming iron plate (5) is attached to root node, the input sensor of robot arm 1 indicates, that there is something there.
			if (rootNode.hasChild(products.get(1).get(5))) {
				sensor_in = "full";
			} else {
				sensor_in = "empty";
			}
			break;
		case 2:
			if (head.hasChild(products.get(1).get(9))) {
				gripperState = "closed";
				capacity = "full";
			} else {
				gripperState = "open";
				capacity = "empty";
			}

			if (rootNode.hasChild(products.get(1).get(1))) {
				sensor_in = "full";
			} else {
				sensor_in = "empty";
			}

			if (belt.hasChild(products.get(1).get(10))) {
				sensor_out = "full";
			} else {
				sensor_out = "empty";
			}
			break;
		case 3:
			if (head.hasChild(products.get(1).get(6))) {
				gripperState = "closed";
				capacity = "full";
			} else {
				gripperState = "open";
				capacity = "empty";
			}

			if (rootNode.hasChild(products.get(1).get(2))) {
				sensor_in = "full";
			} else {
				sensor_in = "empty";
			}

			if (belt.hasChild(products.get(1).get(7))) {
				sensor_out = "full";
			} else {
				sensor_out = "empty";
			}
			break;
		case 4:
			if (head.hasChild(products.get(2).get(3))) {
				gripperState = "closed";
				capacity = "full";
			} else {
				gripperState = "open";
				capacity = "empty";
			}

			if (storagec.get(2) < 3) {
				sensor_out = "empty";
			} else {
				sensor_out = "full";
			}
			if (rootNode.hasChild(products.get(2).get(5))) {
				sensor_in = "full";
			} else {
				sensor_in = "empty";
			}
			break;
		case 5:
			if (head.hasChild(products.get(2).get(6))) {
				gripperState = "closed";
				capacity = "full";
			} else {
				gripperState = "open";
				capacity = "empty";
			}

			if (storagec.get(2) > 0) {
				sensor_in = "full";
			} else {
				sensor_in = "empty";
			}

			if (belt.hasChild(products.get(2).get(7))) {
				sensor_out = "full";
			} else {
				sensor_out = "empty";
			}
			break;
		case 6:
			if (head.hasChild(products.get(3).get(3))) {
				gripperState = "closed";
				capacity = "full";
			} else {
				gripperState = "open";
				capacity = "empty";
			}

			if (storagec.get(3) < 3) {
				sensor_out = "empty";
			} else {
				sensor_out = "full";
			}
			if (rootNode.hasChild(products.get(3).get(5))) {
				sensor_in = "full";
			} else {
				sensor_in = "empty";
			}
			break;
		case 7:
			if (head.hasChild(products.get(3).get(6))) {
				gripperState = "closed";
				capacity = "full";
			} else {
				gripperState = "open";
				capacity = "empty";
			}

			if (storagec.get(3) > 0) {
				sensor_in = "full";
			} else {
				sensor_in = "empty";
			}

			if (belt.hasChild(products.get(3).get(7))) {
				sensor_out = "full";
			} else {
				sensor_out = "empty";
			}
			break;
		case 8:
			if (head.hasChild(products.get(4).get(3))) {
				gripperState = "closed";
				capacity = "full";
			} else {
				gripperState = "open";
				capacity = "empty";
			}

			if (storagec.get(4) < 3) {
				sensor_out = "empty";
			} else {
				sensor_out = "full";
			}
			if (rootNode.hasChild(products.get(4).get(5))) {
				sensor_in = "full";
			} else {
				sensor_in = "empty";
			}
			break;

		}

		if (states == null)
			return Response.status(404).build();

		String state = states.get(id);

		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#state>", true);
		Resource fixstate = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + state + ">", true);
		Resource fixgripperState = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + gripperState + ">",
				true);
		Resource fixcapacity = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + capacity + ">", true);
		Resource sens_in = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + sensor_in + ">", true);
		Resource sens_out = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + sensor_out + ">", true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { self, UKEAQ.hasStatus, fixstate },
				new Node[] { self, UKEAQ.hasGripperState, fixgripperState },
				new Node[] { self, UKEAQ.hasCapacity, fixcapacity },
				new Node[] { self, UKEAQ.hasSensorInState, sens_in },
				new Node[] { self, UKEAQ.hasSensorOutState, sens_out }

		});

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();

	}

	/*
	 * ================================ Belts
	 */

	@GET
	@Path("belt/{id}")
	public Response belt(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Resource doc = new Resource("<" + uInfo.getAbsolutePath().toString() + ">", true);
		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#belt>", true);
		Resource selfLoc = new Resource("<" + uInfo.getAbsolutePath().toString() + "#loc>", true);
		Resource selfLocData = new Resource(
				"<" + uInfo.getAbsolutePath().resolve(id + "/location-data#data").toString() + ">", true);
		Resource selfCoordinateSystem = new Resource("<" + uInfo.getAbsolutePath().toString() + "#selfCS>", true);
		Resource self2worldSpatialRelation = new Resource("<" + uInfo.getAbsolutePath().toString() + "#selfSR>", true);
		Resource worldCoordinateSystem = new Resource(
				"<" + uInfo.getAbsolutePath().resolve("/worldCoordinateSystem#worldCS").toString() + ">", true);
		Resource action = new Resource("<" + uInfo.getAbsolutePath().resolve(id + "/action#action").toString() + ">",
				true);
		Resource state = new Resource("<" + uInfo.getAbsolutePath().resolve(id + "/state#state").toString() + ">",
				true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { doc, FOAF.PRIMARYTOPIC, self },
				new Node[] { self, RDF.TYPE, SCENE.SceneNode },
				new Node[] { self, SCENE.nodeFixedCoordinateSystem, selfCoordinateSystem },
				new Node[] { selfCoordinateSystem, RDF.TYPE, MATHS.CartesianCoordinateSystem },
//			new Node[] { worldCoordinateSystem, RDF.TYPE, MATHS.RhCartesianCoordinateSystem3D },
				new Node[] { self2worldSpatialRelation, RDF.TYPE, SCENE.DefinedSpatialRelationship },
				new Node[] { self2worldSpatialRelation, SPATIAL.sourceCoordinateSystem, worldCoordinateSystem },
				new Node[] { self2worldSpatialRelation, SPATIAL.targetCoordinateSystem, selfCoordinateSystem },
				new Node[] { self2worldSpatialRelation, SPATIAL.translation, selfLoc },
				new Node[] { selfLoc, RDF.TYPE, SPATIAL.Translation3D },
				new Node[] { selfLoc, VOM.quantityValue, selfLocData },
				new Node[] { self, UKEAQ.hasCurrentAction, action },
				new Node[] { self, UKEAQ.hasCurrentState, state } });

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();
	}

	@GET
	@Path("belt/{id}/location-data")
	public Response beltlocation(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, com.jme3.scene.Node> belts = (Hashtable<Integer, com.jme3.scene.Node>) ctx
				.getAttribute(ContextListenerApp.BELT);
		if (belts == null)
			return Response.status(404).build();

		Vector3f vec = ((Spatial) belts.get(id)).getWorldTranslation();

		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#data>", true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { self, RDF.TYPE, MATHS.Vector3D },
				new Node[] { self, MATHS.x, new Literal(Float.toString(vec.getX()), XSD.FLOAT) },
				new Node[] { self, MATHS.y, new Literal(Float.toString(vec.getY()), XSD.FLOAT) },
				new Node[] { self, MATHS.z, new Literal(Float.toString(vec.getZ()), XSD.FLOAT) } });

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();
	}

	@GET
	@Path("belt/{id}/action")
	public Response beltactionG(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, String> anims = (Hashtable<Integer, String>) ctx.getAttribute(ContextListenerApp.BANIMS);
		if (anims == null)
			return Response.status(404).build();

		String action = anims.get(id);

		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#action>", true);
		// Resource actionstatus = new Resource("<" + uInfo.getAbsolutePath().toString()
		// + "#"+ action + ">", true);
		Resource actionstatus = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + action + ">", true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { self, UKEAQ.hasStatus, actionstatus }, });

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();

	}

	@POST
	@Path("belt/{id}/action")
	public Response beltactionP(Iterable<Node[]> nxp, @PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, AnimChannel> channel = (Hashtable<Integer, AnimChannel>) ctx
				.getAttribute(ContextListenerApp.BCHANNEL);
		if (channel == null)
			return Response.status(404).build();

		for (Node[] nx : nxp) {
			if (nx[1].equals(UKEAQ.invokeAction)) {
				if (nx[2].equals(UKEAQ.on)) {
					channel.get(id).setAnim("forward2");
					channel.get(id).setLoopMode(LoopMode.DontLoop);

				} else if (nx[2].equals(UKEAQ.off)) {

				} else if (nx[2].equals(UKEAQ.on_back)) {
					channel.get(id).setAnim("backwards");
					channel.get(id).setLoopMode(LoopMode.DontLoop);
				}

			}

		}

		return Response.noContent().build();

	}

	@GET
	@Path("belt/{id}/state")
	public Response beltstateG(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, String> states = (Hashtable<Integer, String>) ctx.getAttribute(ContextListenerApp.BSTATES);
		Map<Integer, com.jme3.scene.Node> belts = (Hashtable<Integer, com.jme3.scene.Node>) ctx
				.getAttribute(ContextListenerApp.BELT);
		com.jme3.scene.Node belt = (com.jme3.scene.Node) belts.get(id).getChild("clamp.003");

		com.jme3.scene.Node rootNode = (com.jme3.scene.Node) ctx.getAttribute(ContextListenerApp.ROOTNODE);

		Map<Integer, Map<Integer, com.jme3.scene.Node>> products = (Hashtable<Integer, Map<Integer, com.jme3.scene.Node>>) ctx
				.getAttribute(ContextListenerApp.PRODUCTS);

		String sensor_in = "empty";
		String sensor_out = "empty";
		switch (id) {
		case 0:
		case 1:
			if (belt.hasChild(products.get(id).get(4))) {
				sensor_in = "full";
			}
			//the ouput location has the item number 5 not attached to the node of the tranpsort belt, but to the rootNode
			if (rootNode.hasChild(products.get(id).get(5))) {
				sensor_out = "full";
			}
			break;
		case 2:
			if (belt.hasChild(products.get(1).get(10))) {
				sensor_in = "full";
			}
			if (rootNode.hasChild(products.get(1).get(11))) {
				sensor_out = "full";
			}
			break;
		case 3:
			if (belt.hasChild(products.get(1).get(7))) {
				sensor_in = "full";
			}
			if (rootNode.hasChild(products.get(1).get(8))) {
				sensor_out = "full";
			}
			break;
		case 4:
			if (belt.hasChild(products.get(2).get(4))) {
				sensor_in = "full";
			}
			if (rootNode.hasChild(products.get(2).get(5))) {
				sensor_out = "full";
			}
			break;
		case 5:
			if (belt.hasChild(products.get(2).get(7))) {
				sensor_in = "full";
			}
			if (rootNode.hasChild(products.get(2).get(8))) {
				sensor_out = "full";
			}
			break;
		case 6:
			if (belt.hasChild(products.get(3).get(4))) {
				sensor_in = "full";
			}
			if (rootNode.hasChild(products.get(3).get(5))) {
				sensor_out = "full";
			}
			break;
		case 7:
			if (belt.hasChild(products.get(3).get(7))) {
				sensor_in = "full";
			}
			if (rootNode.hasChild(products.get(3).get(8))) {
				sensor_out = "full";
			}
			break;
		case 8:
			if (belt.hasChild(products.get(4).get(4))) {
				sensor_in = "full";
			}
			if (rootNode.hasChild(products.get(4).get(5))) {
				sensor_out = "full";
			}
			break;
		}

		if (states == null)
			return Response.status(404).build();

		String state = states.get(id);

		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#state>", true);
		Resource fixstate = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + state + ">", true);
		Resource sens_in = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + sensor_in + ">", true);
		Resource sens_out = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + sensor_out + ">", true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { self, UKEAQ.hasStatus, fixstate },
				new Node[] { self, UKEAQ.hasSensorInState, sens_in },
				new Node[] { self, UKEAQ.hasSensorOutState, sens_out } });

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();

	}

	/*
	 * ================================= Machines
	 */

	@GET
	@Path("machine/{id}")
	public Response machine(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Resource doc = new Resource("<" + uInfo.getAbsolutePath().toString() + ">", true);
		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#machine>", true);
		Resource selfLoc = new Resource("<" + uInfo.getAbsolutePath().toString() + "#loc>", true);
		Resource selfLocData = new Resource(
				"<" + uInfo.getAbsolutePath().resolve(id + "/location-data#data").toString() + ">", true);
		Resource selfCoordinateSystem = new Resource("<" + uInfo.getAbsolutePath().toString() + "#selfCS>", true);
		Resource self2worldSpatialRelation = new Resource("<" + uInfo.getAbsolutePath().toString() + "#selfSR>", true);
		Resource worldCoordinateSystem = new Resource(
				"<" + uInfo.getAbsolutePath().resolve("/worldCoordinateSystem#worldCS").toString() + ">", true);
		Resource action = new Resource("<" + uInfo.getAbsolutePath().resolve(id + "/action#action").toString() + ">",
				true);
		Resource state = new Resource("<" + uInfo.getAbsolutePath().resolve(id + "/state#state").toString() + ">",
				true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { doc, FOAF.PRIMARYTOPIC, self },
				new Node[] { self, RDF.TYPE, UKEAQ.machine },
				new Node[] { self, SCENE.nodeFixedCoordinateSystem, selfCoordinateSystem },
				new Node[] { selfCoordinateSystem, RDF.TYPE, MATHS.CartesianCoordinateSystem },
//			new Node[] { worldCoordinateSystem, RDF.TYPE, MATHS.RhCartesianCoordinateSystem3D },
				new Node[] { self2worldSpatialRelation, RDF.TYPE, SCENE.DefinedSpatialRelationship },
				new Node[] { self2worldSpatialRelation, SPATIAL.sourceCoordinateSystem, worldCoordinateSystem },
				new Node[] { self2worldSpatialRelation, SPATIAL.targetCoordinateSystem, selfCoordinateSystem },
				new Node[] { self2worldSpatialRelation, SPATIAL.translation, selfLoc },
				new Node[] { selfLoc, RDF.TYPE, SPATIAL.Translation3D },
				new Node[] { selfLoc, VOM.quantityValue, selfLocData },
				new Node[] { self, UKEAQ.hasCurrentAction, action },
				new Node[] { self, UKEAQ.hasCurrentState, state } });

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();
	}

	@GET
	@Path("machine/{id}/location-data")
	public Response machinelocation(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, com.jme3.scene.Node> machines = (Hashtable<Integer, com.jme3.scene.Node>) ctx
				.getAttribute(ContextListenerApp.MACHINES);
		if (machines == null)
			return Response.status(404).build();

		Vector3f vec = ((Spatial) machines.get(id)).getWorldTranslation();

		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#data>", true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { self, RDF.TYPE, MATHS.Vector3D },
				new Node[] { self, MATHS.x, new Literal(Float.toString(vec.getX()), XSD.FLOAT) },
				new Node[] { self, MATHS.y, new Literal(Float.toString(vec.getY()), XSD.FLOAT) },
				new Node[] { self, MATHS.z, new Literal(Float.toString(vec.getZ()), XSD.FLOAT) } });

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();
	}

	@GET
	@Path("machine/{id}/action")
	public Response machineactionG(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, String> actions = (Hashtable<Integer, String>) ctx.getAttribute(ContextListenerApp.MACTION);
		if (actions == null)
			return Response.status(404).build();

		String action = actions.get(id);

		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#action>", true);
		Resource actionstatus = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + action + ">", true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { self, UKEAQ.hasStatus, actionstatus }, });

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();

	}

	@POST
	@Path("machine/{id}/action")
	public Response machineactionP(Iterable<Node[]> nxp, @PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, String> states = (Hashtable<Integer, String>) ctx.getAttribute(ContextListenerApp.MSTATES);
		Map<Integer, String> actions = (Hashtable<Integer, String>) ctx.getAttribute(ContextListenerApp.MACTION);
		Map<Integer, String> machineup = (ConcurrentHashMap<Integer, String>) ctx
				.getAttribute(ContextListenerApp.MACHINEUP);

		if (states == null)
			return Response.status(404).build();

		for (Node[] nx : nxp) {
			if (nx[1].equals(UKEAQ.invokeAction)) {
				if (nx[2].equals(UKEAQ.on)) {
					states.put(id, "on");
					actions.put(id, "moving");
					machineup.put(id, "on");

				} else if (nx[2].equals(UKEAQ.off)) {

				}

			}

		}

		return Response.noContent().build();

	}

	@GET
	@Path("machine/{id}/state")
	public Response machinestateG(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, String> states = (Hashtable<Integer, String>) ctx.getAttribute(ContextListenerApp.MSTATES);
		Map<Integer, com.jme3.scene.Node> belts = (Hashtable<Integer, com.jme3.scene.Node>) ctx
				.getAttribute(ContextListenerApp.BELT);
		com.jme3.scene.Node beltout = (com.jme3.scene.Node) belts.get(1).getChild("clamp.003");

		com.jme3.scene.Node rootNode = (com.jme3.scene.Node) ctx.getAttribute(ContextListenerApp.ROOTNODE);

		Map<Integer, Map<Integer, com.jme3.scene.Node>> products = (Hashtable<Integer, Map<Integer, com.jme3.scene.Node>>) ctx
				.getAttribute(ContextListenerApp.PRODUCTS);
		Map<Integer, com.jme3.scene.Node> iron_ores = products.get(0);
		com.jme3.scene.Node iron_ore = iron_ores.get(4);
		com.jme3.scene.Node iron_ore5 = iron_ores.get(5);

		String sensor_in = "empty";
		String sensor_out = "empty";
		switch (id) {
		case 0:
			if (rootNode.hasChild(iron_ore5)) {
				sensor_in = "full";
			}
			if (beltout.hasChild(products.get(1).get(4))) {
				sensor_out = "full";
			}
			break;
		case 1:
			beltout = (com.jme3.scene.Node) belts.get(4).getChild("clamp.003");
			if (rootNode.hasChild(products.get(1).get(11))) {
				sensor_in = "full";
			}
			if (beltout.hasChild(products.get(2).get(4))) {
				sensor_out = "full";
			}
			break;
		case 2:
			beltout = (com.jme3.scene.Node) belts.get(6).getChild("clamp.003");
			if (rootNode.hasChild(products.get(1).get(8))) {
				sensor_in = "full";
			}
			if (beltout.hasChild(products.get(3).get(4))) {
				sensor_out = "full";
			}
			break;
		case 3:
			beltout = (com.jme3.scene.Node) belts.get(8).getChild("clamp.003");
			if (rootNode.hasChild(products.get(2).get(8)) && rootNode.hasChild(products.get(3).get(8))) {
				sensor_in = "full";
			}
			if (beltout.hasChild(products.get(4).get(4))) {
				sensor_out = "full";
			}
			break;

		}

		if (states == null)
			return Response.status(404).build();

		String state = states.get(id);

		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#state>", true);
		Resource fixstate = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + state + ">", true);
		Resource sens_in = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + sensor_in + ">", true);
		Resource sens_out = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + sensor_out + ">", true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { self, UKEAQ.hasStatus, fixstate },
				new Node[] { self, UKEAQ.hasSensorInState, sens_in },
				new Node[] { self, UKEAQ.hasSensorOutState, sens_out } });

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();

	}


	
	/*
	 * ================================= storage
	 */

	@GET
	@Path("storage/{id}")
	public Response storage(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Resource doc = new Resource("<" + uInfo.getAbsolutePath().toString() + ">", true);
		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#storage>", true);
		Resource selfLoc = new Resource("<" + uInfo.getAbsolutePath().toString() + "#loc>", true);
		Resource selfLocData = new Resource(
				"<" + uInfo.getAbsolutePath().resolve(id + "/location-data#data").toString() + ">", true);
		Resource selfCoordinateSystem = new Resource("<" + uInfo.getAbsolutePath().toString() + "#selfCS>", true);
		Resource self2worldSpatialRelation = new Resource("<" + uInfo.getAbsolutePath().toString() + "#selfSR>", true);
		Resource worldCoordinateSystem = new Resource(
				"<" + uInfo.getAbsolutePath().resolve("/worldCoordinateSystem#worldCS").toString() + ">", true);
		Resource action = new Resource("<" + uInfo.getAbsolutePath().resolve(id + "/action#action").toString() + ">",
				true);
		Resource state = new Resource("<" + uInfo.getAbsolutePath().resolve(id + "/state#state").toString() + ">",
				true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { doc, FOAF.PRIMARYTOPIC, self },
				new Node[] { self, RDF.TYPE, UKEAQ.storage },
				new Node[] { self, SCENE.nodeFixedCoordinateSystem, selfCoordinateSystem },
				new Node[] { selfCoordinateSystem, RDF.TYPE, MATHS.CartesianCoordinateSystem },
//			new Node[] { worldCoordinateSystem, RDF.TYPE, MATHS.RhCartesianCoordinateSystem3D },
				new Node[] { self2worldSpatialRelation, RDF.TYPE, SCENE.DefinedSpatialRelationship },
				new Node[] { self2worldSpatialRelation, SPATIAL.sourceCoordinateSystem, worldCoordinateSystem },
				new Node[] { self2worldSpatialRelation, SPATIAL.targetCoordinateSystem, selfCoordinateSystem },
				new Node[] { self2worldSpatialRelation, SPATIAL.translation, selfLoc },
				new Node[] { selfLoc, RDF.TYPE, SPATIAL.Translation3D },
				new Node[] { selfLoc, VOM.quantityValue, selfLocData },
				new Node[] { self, UKEAQ.hasCurrentAction, action },
				new Node[] { self, UKEAQ.hasCurrentState, state } });

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();
	}

	@GET
	@Path("storage/{id}/location-data")
	public Response storagelocation(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, com.jme3.scene.Node> storages = (Hashtable<Integer, com.jme3.scene.Node>) ctx
				.getAttribute(ContextListenerApp.STORAGE);
		if (storages == null)
			return Response.status(404).build();

		Vector3f vec = ((Spatial) storages.get(id)).getWorldTranslation();

		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#data>", true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { self, RDF.TYPE, MATHS.Vector3D },
				new Node[] { self, MATHS.x, new Literal(Float.toString(vec.getX()), XSD.FLOAT) },
				new Node[] { self, MATHS.y, new Literal(Float.toString(vec.getY()), XSD.FLOAT) },
				new Node[] { self, MATHS.z, new Literal(Float.toString(vec.getZ()), XSD.FLOAT) } });

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();
	}

	@GET
	@Path("storage/{id}/action")
	public Response storageactionG(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, String> actions = (Hashtable<Integer, String>) ctx.getAttribute(ContextListenerApp.SACTION);
		if (actions == null)
			return Response.status(404).build();

		String action = actions.get(id);

		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#action>", true);
		Resource actionstatus = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + action + ">", true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { self, UKEAQ.hasStatus, actionstatus }});

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();

	}

	@POST
	@Path("storage/{id}/action")
	public Response storageactionP(Iterable<Node[]> nxp, @PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, String> actions = (Hashtable<Integer, String>) ctx.getAttribute(ContextListenerApp.SACTION);
		Map<Integer, String> storageup = (ConcurrentHashMap<Integer, String>) ctx
				.getAttribute(ContextListenerApp.STORAGEUP);

		for (Node[] nx : nxp) {
			if (nx[1].equals(UKEAQ.invokeAction)) {
				if (nx[2].equals(UKEAQ.fill)) {
					actions.put(id, "busy");
					storageup.put(id, "fill");

				}  else if (nx[2].equals(UKEAQ.empty)) {
					actions.put(id, "busy");
					storageup.put(id, "empty");
					} else return Response.status(404).build();


			} else return Response.status(404).build();

		}

		return Response.noContent().build();

	}

	@GET
	@Path("storage/{id}/state")
	public Response storagestateG(@PathParam(value = "id") int id, @Context UriInfo uInfo) {

		Map<Integer, String> states = (Hashtable<Integer, String>) ctx.getAttribute(ContextListenerApp.SSTATES);

		com.jme3.scene.Node rootNode = (com.jme3.scene.Node) ctx.getAttribute(ContextListenerApp.ROOTNODE);

		Map<Integer, Map<Integer, com.jme3.scene.Node>> products = (Hashtable<Integer, Map<Integer, com.jme3.scene.Node>>) ctx.getAttribute(ContextListenerApp.PRODUCTS);
		
		if (states == null)
			return Response.status(404).build();
		
		com.jme3.scene.Node product0 = products.get(id).get(0);
		com.jme3.scene.Node product2 = products.get(id).get(2);

		String capacity = "";
		if (rootNode.hasChild(product2)) {
			capacity = "full";
		}
		else if (rootNode.hasChild(product0)){
			capacity = "non-empty";
		}
		else capacity = "empty";

		

		String state = states.get(id);

		Resource self = new Resource("<" + uInfo.getAbsolutePath().toString() + "#state>", true);
		Resource fixstate = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + state + ">", true);
		Resource fixcapacity = new Resource("<http://www.student.kit.edu/~ukeaq/uni/voc.ttl#" + capacity + ">", true);

		Iterable<Node[]> l = Arrays.asList(new Node[][] { new Node[] { self, UKEAQ.hasStatus, fixstate },
			new Node[] { self, UKEAQ.hasCapacity, fixcapacity }});

		return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
		}).build();

	}
}
