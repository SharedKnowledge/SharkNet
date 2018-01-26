package net.sharksystem.sharknet;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.util.Log;

import junit.framework.Assert;

import net.sharkfw.knowledgeBase.PeerTaxonomy;
import net.sharkfw.knowledgeBase.SemanticNet;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TimeSTSet;
import net.sharkfw.knowledgeBase.broadcast.Dimension;
import net.sharkfw.knowledgeBase.broadcast.SpatialFilter;
import net.sharkfw.knowledgeBase.geom.SharkPoint;
import net.sharkfw.knowledgeBase.inmemory.InMemoGenericTagStorage;
import net.sharkfw.knowledgeBase.inmemory.InMemoPeerSemanticNet;
import net.sharkfw.knowledgeBase.inmemory.InMemoPeerTaxonomy;
import net.sharkfw.knowledgeBase.inmemory.InMemoSemanticNet;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.knowledgeBase.inmemory.InMemoSpatialSTSet;
import net.sharkfw.knowledgeBase.inmemory.InMemoTimeSTSet;
import net.sharksystem.sharknet.data.dataprovider.SQLPolygonDataProvider;
import net.sharksystem.sharknet.location.LastLocationImpl;
import net.sharksystem.sharknet.locationprofile.PolygonLocationProfile;
import net.sharksystem.sharknet.locationprofile.SharkServiceBinder;
import net.sharksystem.sharknet.service.LocationProfilingService;
import net.sharksystem.sharknet.service.ServiceController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Max on 21.01.18.
 *
 * @author Max Oehme (546545)
 */
@RunWith(Parameterized.class)
@SmallTest
public class SpatialFilterUnitTest {
    private final String TAG = this.getClass().getSimpleName();

    private static final int TESTCOUNT = 100;
    private static final int MAXLOCATIONRADIUS = 100000;

    private Context mContext;
    private SpatialFilter spatialFilter;
    private SemanticNet topic;
    private PeerTaxonomy peer;
    private InMemoSpatialSTSet spatial;
    private TimeSTSet time;
    private SharkPoint point;

    public SpatialFilterUnitTest(SharkPoint point) {
        this.point = point;
    }

    @Parameterized.Parameters
    public static List<SharkPoint> configParameters() {
        LastLocationImpl loc = new LastLocationImpl(InstrumentationRegistry.getTargetContext());

        List<SharkPoint> pointList = new ArrayList<>();
        for (int i=0;i<TESTCOUNT;i++) {
            pointList.add(getLocation(loc.getLastLocation().getY(), loc.getLastLocation().getX(), MAXLOCATIONRADIUS));
        }

        return pointList;
    }

    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getTargetContext();
        SharkServiceBinder exec = new ServiceController(mContext, LocationProfilingService.class);
        spatialFilter = new SpatialFilter(Dimension.SPATIAL, new PolygonLocationProfile(new SQLPolygonDataProvider(mContext), new LastLocationImpl(mContext), exec), 0.5);

        topic = new InMemoSemanticNet(new InMemoGenericTagStorage());
        peer = new InMemoPeerTaxonomy(new InMemoPeerSemanticNet(new InMemoGenericTagStorage()));
        spatial = new InMemoSpatialSTSet(new InMemoGenericTagStorage());
        time = new InMemoTimeSTSet(new InMemoGenericTagStorage());
    }

    @Test
    public void randomLocationFilterTest(){
        try {
            String[] sis = new String[]{""};
            spatial.addGeoSemanticTag(spatial.createSpatialSemanticTag("testPoint", sis, point));
            SharkKB knowledge = new InMemoSharkKB(topic, topic, peer, spatial, time);

            boolean interesting = spatialFilter.filter(null, knowledge, null);

            Assert.assertTrue(interesting);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }


    /**
     * Generate random coordinates in radius
     * Small modifcations but code written by user MikeJRamsey56 on StackExchange
     * https://gis.stackexchange.com/a/68275
     * @param x0 Longitude
     * @param y0 Latitude
     * @param radius
     */
    public static SharkPoint getLocation(double x0, double y0, int radius) {
        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(Math.toRadians(y0));

        double foundLongitude = new_x + x0;
        double foundLatitude = y + y0;
        return new SharkPoint(foundLatitude, foundLongitude);
    }
}
