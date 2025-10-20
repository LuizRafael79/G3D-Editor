/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package g3deditor;

import g3deditor.geo.GeoBlockSelector;
import g3deditor.geo.GeoEngine;
import g3deditor.jogl.GLDisplay;
import g3deditor.swing.FrameMain;
import g3deditor.swing.Splash;
import g3deditor.swing.Splash.CheckedRunnable;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;

import com.jogamp.opengl.util.Animator;

/**
 * <a href="http://l2j-server.com/">L2jServer</a>
 * 
 * @author Forsaiken aka Patrick, e-mail: patrickbiesenbach@yahoo.de
 */
public final class G3DEditor
{
	public static final void main(final String[] args)
	{
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		
		Config.load();
		
		new Splash(3000,
		new Runnable()
		{
			@Override
			public final void run()
			{
				System.out.println("OpenGL Test");
				String[] profiles = {
				    "GL2",
				    "GL2GL3",
				    "GL2ES1",
				    "GL2ES2",
				    "GL2ES3",
				    "GL2EGL",
				    "GL3",
				    "GL3bc",
				    "GL4",
				    "GL4bc",
				    "GL4ES3",
				    "GLES1",
				    "GLES2",
				    "GLES3"
				};

				for (String p : profiles) {
				    boolean available = GLProfile.isAvailable(p);
				    System.out.printf("%-8s : %s%n", p, (available ? "Yes" : "No"));
				}
				
			    if (!GLProfile.isAvailable(GLProfile.GL2))
					throw new RuntimeException("OpenGL2 is required to run this software");
				
				if (!GLProfile.isAWTAvailable())
					throw new RuntimeException("AWT support is required to run this software");
				
				GeoEngine.init();
				GeoBlockSelector.init();
				
				GLProfile.initSingleton();
				GLProfile profile = GLProfile.get(GLProfile.GL4bc);
				GLCapabilities caps = new GLCapabilities(profile);
				caps.setOnscreen(true);
				caps.setDoubleBuffered(true);
				caps.setHardwareAccelerated(true);
				caps.setSampleBuffers(false);
				caps.setNumSamples(0);
				GLCanvas canvas = new GLCanvas(caps);
				GLDisplay.init(canvas);
				canvas.addGLEventListener(GLDisplay.getInstance());
				FrameMain.init();
			}
		},
		new CheckedRunnable()
		{
			@Override
			public final boolean run()
			{
				if (GeoEngine.getInstance() == null)
					return false;
				
				if (GeoBlockSelector.getInstance() == null)
					return false;
				
				if (FrameMain.getInstance() == null)
					return false;
				return true;
			}
		},
		new Runnable()
		{
			@Override
			public final void run()
			{
				FrameMain.getInstance().validate();
				FrameMain.getInstance().setVisible(true);
				new Animator(GLDisplay.getInstance().getCanvas()).start();
			}
		});
	}
}