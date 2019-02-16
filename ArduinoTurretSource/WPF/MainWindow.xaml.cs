using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

using Emgu.CV;
using Emgu.Util;
using Emgu.CV.Structure;
using Emgu.CV.CvEnum;

using System.Runtime.InteropServices;
using System.Diagnostics;
using System.IO.Ports;

namespace EmguCV_WPF_Test
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private SoundSystem m_soundSystem = new SoundSystem();

        public MainWindow()
        {
            InitializeComponent();

            // Create Timer that updates until the application closes
            System.Windows.Threading.DispatcherTimer dispatcherTimer = new System.Windows.Threading.DispatcherTimer();
            dispatcherTimer.Tick += dispatcherTimer_Tick;
            dispatcherTimer.Interval = new TimeSpan(0, 0, 0, 0, 1);
            dispatcherTimer.Start();

            // Initialize camera i uvedi 'detection.xml' u m_cascade 
            m_capture = new VideoCapture();
            m_cascade = new CascadeClassifier("faceAlt2.xml");

            // Play Starting Deploy Sound
            m_soundSystem.Deploy();
            m_soundSystem.UseSoundSystem(false);

            m_serialPort.Open();
            m_stopwatch2.Start();
        }

        private void dispatcherTimer_Tick(object sender, EventArgs e)
        {
            // Uzima trenutni frame
            using (Image<Bgr, byte> nextFrame = m_capture.QueryFrame().ToImage<Bgr, Byte>())
            {
                if (nextFrame != null)
                {
                    // Grayscale image
                    Image<Gray, byte> grayframe = nextFrame.Convert<Gray, byte>();

                    // Resize grayframe for better performance
                    grayframe.Resize(0.25, Inter.Linear);

                    // Scan for faces
                    var faces = m_cascade.DetectMultiScale(grayframe, 1.1, 3, new System.Drawing.Size(20, 20), System.Drawing.Size.Empty);

                    // Add face rect to image
                    foreach (var face in faces)
                    {
                        nextFrame.Draw(face, new Bgr(0, 255, 0), 3);
                    }

                    // Make focused face have a red rectangle
                    if (faces.Length != 0)
                    {
                        nextFrame.Draw(faces[0], new Bgr(0, 0, 255), 3);
                        sendtoTurret(faces);
                    }
                   
                    // Display frame with face rectangle
                    m_image.Source = ToBitmapSource(nextFrame);

                    // Play Sounds
                    SoundEffects(faces);
                }
            }
        }

        // Sound Effects Code
        private void SoundEffects(System.Drawing.Rectangle[] faces)
        {
            if (faces.Length == 0)
            {
                if (!m_Detected)
                {
                    m_stopwatch.Start();
                }

                m_Detected = false;
            }
            else if (faces.Length != 0)
            {
                if (!m_Detected)
                {
                    m_soundSystem.Detected();
                }

                m_Detected = true;
            }
            if (!m_Detected && m_stopwatch.ElapsedMilliseconds >= 30000)
            {
                m_stopwatch.Restart();

                m_soundSystem.Search();
            }
        }

        // Sending info to turret - arduino
        private void sendtoTurret(System.Drawing.Rectangle[] faces)
        {
            // Translat-a cordinatni sustav da je sredina 0,0 i pretvara kordinate u kordinate od 0,180
            float X, Y;
            X = ((float)(faces[0].X + faces[0].Width / 2) - (float)m_Window.Width / 2) * (180f / (float)m_Window.Width);
            Y = ((float)(faces[0].Y + faces[0].Height / 2) - (float)m_Window.Height / 2) * (180f / (float)m_Window.Height);

            string COORD = String.Format("{0},{1}", (int)X, (int)Y);

            MyLabel.Content = COORD;

            // Delay 500ms and send to arduino turret
            if (m_stopwatch2.ElapsedMilliseconds >= 500)
            {
                m_stopwatch.Restart();
                m_serialPort.Write(COORD);
            }
        }

        // Variables used for Sound Effect Code
        private bool m_Detected = false;
        private Stopwatch m_stopwatch = new Stopwatch();

        // For sending info to turret - arduino
        private Stopwatch m_stopwatch2 = new Stopwatch();
        private SerialPort m_serialPort = new SerialPort("COM4");

        private VideoCapture m_capture;
        private CascadeClassifier m_cascade;

        //////////////////////////////////////////////////////////////////////////////////////

        // Convert Bitmap to WPF BitmapSource
        [DllImport("gdi32")]
        private static extern int DeleteObject(IntPtr o);

        public static BitmapSource ToBitmapSource(IImage image)
        {
            using (System.Drawing.Bitmap source = image.Bitmap)
            {
                IntPtr ptr = source.GetHbitmap(); //obtain the Hbitmap  

                BitmapSource bs = System.Windows.Interop
                  .Imaging.CreateBitmapSourceFromHBitmap(
                  ptr,
                  IntPtr.Zero,
                  Int32Rect.Empty,
                  System.Windows.Media.Imaging.BitmapSizeOptions.FromEmptyOptions());

                DeleteObject(ptr); //release the HBitmap  
                return bs;
            }
        }
    }
}
