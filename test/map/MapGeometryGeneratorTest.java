package map;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import geometry.Point2D;
import geometry.Rect2D;
import math.FpUtil;

public class MapGeometryGeneratorTest {

	private static boolean verifyTileNeighbors(MapTile tile, int[] expected) {
		if (tile.countNeighbors() != expected.length)
			return false;
		for (int i = 0; i < tile.countNeighbors(); ++i)
			if (tile.neighbor(i) != expected[i])
				return false;
		return true;
	}
	
	private static boolean verifyNodeNeighbors(MapNode node, int[] expected) {
		if (node.countNeighbors() != expected.length)
			return false;
		for (int i = 0; i < node.countNeighbors(); ++i)
			if (node.neighbor(i) != expected[i])
				return false;
		return true;
	}
	
	private static boolean verifyNodeCoordinates(MapNode node, double[] expected) {
		return FpUtil.fpEqual(node.pos.x, expected[0]) &&
				FpUtil.fpEqual(node.pos.y, expected[1]);
	}
	
	@Test
	public void generate_TileNeighbors() {
		var bounds = new Rect2D(10, 10, 15, 15);
		var map = new Map(null, null);
		var spec = new MapGeometryGenerator.Spec(bounds, 1, 30);
		var gen = new MapGeometryGenerator(map, spec);
		
		List<Point2D> samples = new ArrayList<Point2D>();
		samples.add(new Point2D(12.066096133148083, 14.952071865649085));
		samples.add(new Point2D(10.359849814108108, 14.049893453244641));
		samples.add(new Point2D(13.653193719274324, 14.339350751718165));
		samples.add(new Point2D(11.84474718538761, 13.181846436135407));
		samples.add(new Point2D(13.456819983470801, 12.846725798122872));
		samples.add(new Point2D(14.594267081916872, 12.774468009475912));
		samples.add(new Point2D(11.47279168687508, 11.606484000770104));
		samples.add(new Point2D(10.198911427928012, 12.149143937412557));
		samples.add(new Point2D(13.483368093737289, 11.083084116266384));
		samples.add(new Point2D(14.849570135024054, 10.946546502471405));
		samples.add(new Point2D(11.557253434315392, 10.25812270703529));
		samples.add(new Point2D(10.175640410810594, 10.537547103975443));

		Map.Representation rep = gen.generate(samples);
		
		// If the following two asserts fail, then most likely the random
		// number generation in the code has changed, e.g. a call to Random
		// got added or removed, or the geometry generation itself changed.
		assertEquals(12, rep.countTiles());
		assertEquals(26, rep.countNodes());
		
		assertTrue(verifyTileNeighbors(rep.tile(0), new int[] {3, 1, 4}));
		assertTrue(verifyTileNeighbors(rep.tile(1), new int[] {0, 3, 5, 2}));
		assertTrue(verifyTileNeighbors(rep.tile(2), new int[] {1, 5, 6}));
		assertTrue(verifyTileNeighbors(rep.tile(3), new int[] {0, 1, 4, 5, 7, 8}));
		// The neighoring relationship with tile 11 happens outside the given bounds!
		// This might cause problems when defining tile properties.
		assertTrue(verifyTileNeighbors(rep.tile(4), new int[] {0, 3, 8, 11}));
		assertTrue(verifyTileNeighbors(rep.tile(5), new int[] {1, 2, 3, 6, 7, 9}));
		assertTrue(verifyTileNeighbors(rep.tile(6), new int[] {5, 2, 9}));
		assertTrue(verifyTileNeighbors(rep.tile(7), new int[] {3, 5, 8, 9, 10}));
		assertTrue(verifyTileNeighbors(rep.tile(8), new int[] {4, 3, 7, 10, 11}));
		assertTrue(verifyTileNeighbors(rep.tile(9), new int[] {7, 5, 6, 10}));
		assertTrue(verifyTileNeighbors(rep.tile(10), new int[] {8, 7, 9, 11}));
		// The neighoring relationship with tile 4 happens outside the given bounds!
		// This might cause problems when defining tile properties.
		assertTrue(verifyTileNeighbors(rep.tile(11), new int[] {4, 8, 10}));
	}
	
	@Test
	public void generate_Nodes() {
		var bounds = new Rect2D(10, 10, 15, 15);
		var map = new Map(null, null);
		var spec = new MapGeometryGenerator.Spec(bounds, 1, 30);
		var gen = new MapGeometryGenerator(map, spec);
		
		List<Point2D> samples = new ArrayList<Point2D>();
		samples.add(new Point2D(12.066096133148083, 14.952071865649085));
		samples.add(new Point2D(10.359849814108108, 14.049893453244641));
		samples.add(new Point2D(13.653193719274324, 14.339350751718165));
		samples.add(new Point2D(11.84474718538761, 13.181846436135407));
		samples.add(new Point2D(13.456819983470801, 12.846725798122872));
		samples.add(new Point2D(14.594267081916872, 12.774468009475912));
		samples.add(new Point2D(11.47279168687508, 11.606484000770104));
		samples.add(new Point2D(10.198911427928012, 12.149143937412557));
		samples.add(new Point2D(13.483368093737289, 11.083084116266384));
		samples.add(new Point2D(14.849570135024054, 10.946546502471405));
		samples.add(new Point2D(11.557253434315392, 10.25812270703529));
		samples.add(new Point2D(10.175640410810594, 10.537547103975443));

		Map.Representation rep = gen.generate(samples);
		
		// If the following two asserts fail, then most likely the random
		// number generation in the code has changed, e.g. a call to Random
		// got added or removed, or the geometry generation itself changed.
		assertEquals(12, rep.countTiles());
		assertEquals(26, rep.countNodes());
		
		assertTrue(rep.node(0).pos.equals(new Point2D(9.9999999999, 11.3460497337)));
		assertTrue(rep.node(1).pos.equals(new Point2D(10.60560028859, 11.3373050313)));
		assertTrue(rep.node(2).pos.equals(new Point2D(10.9676054642, 10.8980129862)));
		assertTrue(rep.node(3).pos.equals(new Point2D(10.7859867816, 10.0)));
		assertTrue(rep.node(4).pos.equals(new Point2D(10.0, 10.0)));
		assertTrue(rep.node(5).pos.equals(new Point2D(10.0, 13.1231741354)));
		assertTrue(rep.node(6).pos.equals(new Point2D(10.7758728712, 13.05748019376)));
		assertTrue(rep.node(7).pos.equals(new Point2D(11.11091486033, 12.52351800564)));
		assertTrue(rep.node(8).pos.equals(new Point2D(10.94911735851, 14.9999999999)));
		assertTrue(rep.node(9).pos.equals(new Point2D(11.40614787257, 14.13564033278)));
		assertTrue(rep.node(10).pos.equals(new Point2D(10.0, 15.0)));
		assertTrue(rep.node(11).pos.equals(new Point2D(12.4814904813, 12.1999142895)));
		assertTrue(rep.node(12).pos.equals(new Point2D(12.63624424468, 11.9523530105)));
		assertTrue(rep.node(13).pos.equals(new Point2D(12.3848861816, 10.98679186659)));
		assertTrue(rep.node(14).pos.equals(new Point2D(12.80753246851, 10.0)));
		assertTrue(rep.node(15).pos.equals(new Point2D(12.60485903445, 13.9857535161)));
		assertTrue(rep.node(16).pos.equals(new Point2D(12.7919644602, 13.69342617514)));
		assertTrue(rep.node(17).pos.equals(new Point2D(12.9964230068, 15.0)));
		assertTrue(rep.node(18).pos.equals(new Point2D(14.0709371418, 13.52516110436)));
		assertTrue(rep.node(19).pos.equals(new Point2D(13.97230010537, 11.9724646685)));
		assertTrue(rep.node(20).pos.equals(new Point2D(14.06504893156, 10.0)));
		assertTrue(rep.node(21).pos.equals(new Point2D(14.24432067875, 11.79380187002)));
		assertTrue(rep.node(22).pos.equals(new Point2D(15.0, 15.0)));
		assertTrue(rep.node(23).pos.equals(new Point2D(15.0, 14.08387153324)));
		assertTrue(rep.node(24).pos.equals(new Point2D(15.0, 11.89934646651)));
		assertTrue(rep.node(25).pos.equals(new Point2D(15.0, 10.0)));
	}
	
	@Test
	public void generate_NodeNeighbors() {
		var bounds = new Rect2D(10, 10, 15, 15);
		var map = new Map(null, null);
		var spec = new MapGeometryGenerator.Spec(bounds, 1, 30);
		var gen = new MapGeometryGenerator(map, spec);
		
		List<Point2D> samples = new ArrayList<Point2D>();
		samples.add(new Point2D(12.066096133148083, 14.952071865649085));
		samples.add(new Point2D(10.359849814108108, 14.049893453244641));
		samples.add(new Point2D(13.653193719274324, 14.339350751718165));
		samples.add(new Point2D(11.84474718538761, 13.181846436135407));
		samples.add(new Point2D(13.456819983470801, 12.846725798122872));
		samples.add(new Point2D(14.594267081916872, 12.774468009475912));
		samples.add(new Point2D(11.47279168687508, 11.606484000770104));
		samples.add(new Point2D(10.198911427928012, 12.149143937412557));
		samples.add(new Point2D(13.483368093737289, 11.083084116266384));
		samples.add(new Point2D(14.849570135024054, 10.946546502471405));
		samples.add(new Point2D(11.557253434315392, 10.25812270703529));
		samples.add(new Point2D(10.175640410810594, 10.537547103975443));

		Map.Representation rep = gen.generate(samples);
		
		// If the following two asserts fail, then most likely the random
		// number generation in the code has changed, e.g. a call to Random
		// got added or removed, or the geometry generation itself changed.
		assertEquals(12, rep.countTiles());
		assertEquals(26, rep.countNodes());
		
		assertTrue(verifyNodeNeighbors(rep.node(0), new int[] {1, 4, 5}));
		assertTrue(verifyNodeNeighbors(rep.node(1), new int[] {0, 2, 7}));
		assertTrue(verifyNodeNeighbors(rep.node(2), new int[] {1, 3, 13}));
		assertTrue(verifyNodeNeighbors(rep.node(3), new int[] {2, 4, 14}));
		assertTrue(verifyNodeNeighbors(rep.node(4), new int[] {3, 0}));
		assertTrue(verifyNodeNeighbors(rep.node(5), new int[] {6, 0, 10}));
		assertTrue(verifyNodeNeighbors(rep.node(6), new int[] {5, 7, 9}));
		assertTrue(verifyNodeNeighbors(rep.node(7), new int[] {6, 1, 11}));
		assertTrue(verifyNodeNeighbors(rep.node(8), new int[] {9, 10, 17}));
		assertTrue(verifyNodeNeighbors(rep.node(9), new int[] {8, 6, 15}));
		assertTrue(verifyNodeNeighbors(rep.node(10), new int[] {5, 8}));
		assertTrue(verifyNodeNeighbors(rep.node(11), new int[] {7, 12, 16}));
		assertTrue(verifyNodeNeighbors(rep.node(12), new int[] {11, 13, 19}));
		assertTrue(verifyNodeNeighbors(rep.node(13), new int[] {12, 2, 14}));
		assertTrue(verifyNodeNeighbors(rep.node(14), new int[] {3, 13, 20}));
		assertTrue(verifyNodeNeighbors(rep.node(15), new int[] {9, 16, 17}));
		assertTrue(verifyNodeNeighbors(rep.node(16), new int[] {15, 11, 18}));
		assertTrue(verifyNodeNeighbors(rep.node(17), new int[] {8, 15, 22}));
		assertTrue(verifyNodeNeighbors(rep.node(18), new int[] {16, 19, 23}));
		assertTrue(verifyNodeNeighbors(rep.node(19), new int[] {18, 12, 21}));
		assertTrue(verifyNodeNeighbors(rep.node(20), new int[] {14, 21, 25}));
		assertTrue(verifyNodeNeighbors(rep.node(21), new int[] {19, 20, 24}));
		assertTrue(verifyNodeNeighbors(rep.node(22), new int[] {17, 23}));
		assertTrue(verifyNodeNeighbors(rep.node(23), new int[] {22, 18, 24}));
		assertTrue(verifyNodeNeighbors(rep.node(24), new int[] {23, 21, 25}));
		assertTrue(verifyNodeNeighbors(rep.node(25), new int[] {24, 20}));
	}
}
