using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Emgu.CV;
using Emgu.CV.UI;
using Emgu.CV.Structure;
using System.Drawing;
using System.Windows.Forms;

namespace EmguCV_Console_Test
{
    class Program
    {
        static void Main(string[] args)
        {
            ImageViewer imageViewer = new ImageViewer();
            VideoCapture videoCapture = new VideoCapture();

            Application.Idle += new EventHandler(delegate (object sender, EventArgs e)
            {
                imageViewer.Image = videoCapture.QueryFrame();
            });
            imageViewer.ShowDialog();
        }
    }
}
