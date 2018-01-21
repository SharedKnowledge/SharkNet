package net.sharksystem.sharknet;

import android.app.Application;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import net.sharkfw.asip.engine.ASIPInMessage;
import net.sharkfw.knowledgeBase.PeerSemanticNet;
import net.sharkfw.knowledgeBase.PeerTaxonomy;
import net.sharkfw.knowledgeBase.SemanticNet;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.SpatialSTSet;
import net.sharkfw.knowledgeBase.SpatialSemanticTag;
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
import net.sharkfw.knowledgeBase.persistent.dump.DumpSharkKB;
import net.sharkfw.knowledgeBase.persistent.sql.SqlSharkKB;
import net.sharkfw.knowledgeBase.sync.SyncKB;
import net.sharksystem.sharknet.data.dataprovider.SQLPolygonDataProvider;
import net.sharksystem.sharknet.location.LastLocationImpl;
import net.sharksystem.sharknet.locationprofile.PolygonLocationProfile;
import net.sharksystem.sharknet.locationprofile.SharkServiceBinder;
import net.sharksystem.sharknet.profile.EntryProfileActivity;
import net.sharksystem.sharknet.service.LocationProfilingService;
import net.sharksystem.sharknet.service.ServiceController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Max on 21.01.18.
 *
 * @author Max Oehme (546545)
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class SpatialFilterUnitTest {
    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private SpatialFilter spatialFilter;
    private SemanticNet topic;
    private PeerTaxonomy peer;
    private InMemoSpatialSTSet spatial;
    private TimeSTSet time;

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
    public void filterTest() {
        Log.e(TAG, "Test Starting");

        try {
            String[] sis = new String[]{""};
            spatial.addGeoSemanticTag(spatial.createSpatialSemanticTag("testPoint", sis, new SharkPoint(52.586657, 13.410188)));
            SharkKB knowledge = new InMemoSharkKB(topic, topic, peer, spatial, time);

            boolean interesting = spatialFilter.filter(null, knowledge, null);

            Assert.assertTrue(interesting);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }
}
