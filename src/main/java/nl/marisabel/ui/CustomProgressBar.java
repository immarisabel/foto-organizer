package nl.marisabel.ui;


import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Clase que modela un ProgressBar como panel
 *
 * @author d.narvaez11
 */
public class CustomProgressBar extends JPanel
{
    /**
     * Thread que ejecuta los proceso de posicion y pintado de la barra de progreso
     *
     * @author d.narvaez11
     */
    private class ThreadProgreso extends Thread
    {
        /**
         * Progreso a pintar
         */
        private long pProgress;

        /**
         * Delay de movimiento
         */
        private long timeDelay;


        public ThreadProgreso( long pProgreso, long timeDelay )
        {
            pProgress = pProgreso;
            this.timeDelay = timeDelay;
        }

        @Override
        public void run( )
        {
            setProgressBar( pProgress, timeDelay );

            int onQueue = threadsOnQueue.size( );
            if( onQueue > 0 )
            {
                // System.out.println( "onQueue Resolved: " + ( ++queueResolved ) );
                threadsOnQueue.get( 0 ).start( );
                threadsOnQueue.remove( 0 );
                onQueue = threadsOnQueue.size( );
                if( ( onEmptyQueueListener != null ) && ( onQueue == 0 ) )
                {
                    onEmptyQueueListener.OnEmptyQueue( );
                }
            }
        }

        @Override
        public void start( )
        {
            threadActive = true;
            super.start( );
        }
    }

    private static final long serialVersionUID = -7392056679892630730L;

    /**
     * For console printing
     */
    // private int queueResolved;

    /**
     * Color del progreso faltante del ProgressBar
     */
    private Color colorComplemento;

    /**
     * Color del progreso del ProgressBar
     */
    private Color colorProgreso;

    /**
     * Imagen que se muestra cuando la barra de progreso llega a un 100%
     */
    private ImageIcon endImage;

    /**
     * Imagen de la barra de progreso
     */
    private ImageIcon imageBar;

    /**
     * Imagen que lidera la barra de progreso
     */
    private ImageIcon imageIcon;

    /**
     * FinishOnQueueListener que es notificado cuando no hay Threads en cola
     */
    private OnEmptyQueueListener onEmptyQueueListener;

    /**
     * Progreso del ProgressBar
     */
    private double progress;

    /**
     * Verifica si hay algun thread corriendo
     */
    private boolean threadActive;

    /**
     * Lista de threads en cola
     */
    private ArrayList<Thread> threadsOnQueue;

    /**
     * Constructor del ProgressBar. <br>
     * Establece los colores:
     * <ul>
     * <li>colorComplemento = Color.WHITE
     * <li>colorProgreso = Color.GRAY
     * </ul>
     */
    public CustomProgressBar( )
    {
        threadsOnQueue = new ArrayList<>( );
        colorComplemento = Color.WHITE;
        colorProgreso = Color.GRAY;
    }

    /**
     * Establece el OnEmptyQueueListener, que es notificado cuando no hay Threads en cola
     *
     * @param finishListener OnEmptyQueueListener que será notificado
     */
    public void addOnEmptyQueueListener( OnEmptyQueueListener finishListener )
    {
        onEmptyQueueListener = finishListener;
    }

    /**
     * Verifica si hay Threads en cola
     *
     * @return Ture si hay Threads en cola, False de lo contrario
     */
    public boolean hasOnQueue( )
    {
        return threadsOnQueue.size( ) > 0;
    }

    @Override
    public void paint( Graphics g )
    {
        super.paint( g );
        Graphics2D g2d = ( Graphics2D ) g;
        int pos = ( ( int ) progress * getWidth( ) ) / 100;

        if( imageBar == null )
        {
            g2d.setColor( colorComplemento );
            Rectangle2D.Double back = new Rectangle2D.Double( 0, 0, getWidth( ), getHeight( ) );
            g2d.fill( back );

            g2d.setColor( colorProgreso );
            Rectangle2D.Double rec = new Rectangle2D.Double( 0, 0, pos, getHeight( ) );
            g2d.fill( rec );

            if( imageIcon != null )
            {
                int x0 = pos - ( imageIcon.getIconWidth( ) / 2 );
                int y0 = ( getHeight( ) / 2 ) - ( imageIcon.getIconHeight( ) / 2 );

                if( ( pos == getWidth( ) ) && ( endImage != null ) )
                {
                    x0 = pos - endImage.getIconWidth( );
                    y0 = ( getHeight( ) / 2 ) - ( endImage.getIconHeight( ) / 2 );
                    g2d.drawImage( endImage.getImage( ), x0, y0, null );
                }
                else
                {
                    g2d.drawImage( imageIcon.getImage( ), x0, y0, null );
                }
            }
        }
        else
        {
            g2d.setColor( colorProgreso );
            Rectangle2D.Double back = new Rectangle2D.Double( 0, 0, getWidth( ), getHeight( ) );
            g2d.fill( back );

            g2d.drawImage( imageBar.getImage( ), 0, 0, getWidth( ), getHeight( ), null );

            g2d.setColor( colorComplemento );
            Rectangle2D.Double rec = new Rectangle2D.Double( pos, 0, getWidth( ), getHeight( ) );
            g2d.fill( rec );

            if( imageIcon != null )
            {
                int x0 = pos - ( imageIcon.getIconWidth( ) / 2 );
                int y0 = ( getHeight( ) / 2 ) - ( imageIcon.getIconHeight( ) / 2 );

                if( ( pos == getWidth( ) ) && ( endImage != null ) )
                {
                    x0 = pos - endImage.getIconWidth( );
                    y0 = ( getHeight( ) / 2 ) - ( endImage.getIconHeight( ) / 2 );
                    g2d.drawImage( endImage.getImage( ), x0, y0, null );
                }
                else
                {
                    g2d.drawImage( imageIcon.getImage( ), x0, y0, null );
                }
            }
        }
    }

    /**
     * Establece el color de complemento con el que entra por parámetro
     *
     * @param colorComplemento Color de complemento
     */
    public void setColorComplemento( Color colorComplemento )
    {
        this.colorComplemento = colorComplemento;
    }

    /**
     * Establece el color de progreso con el que entra por parametro
     *
     * @param colorProgreso Color de progreso
     */
    public void setColorProgreso( Color colorProgreso )
    {
        this.colorProgreso = colorProgreso;
    }

    /**
     * Establece la imagen cursor que se mostrará cuando la barra de progreso llegue a un 100%
     *
     * @param endImage Imagen cursor que se mostrará cuando la barra de progreso llegue a un 100%
     */
    public void setEndImage( ImageIcon endImage )
    {
        this.endImage = endImage;
    }

    /**
     * Establece la imagen de la barra de progreso.<br>
     * Se establece el PreferredSize como la dimension de la imagen que entra por parámetro.<br>
     * Sin embargo el tamaño del Panel puede variar y lo hará la barra de progreso
     *
     * @param image Imagen de la barra de progreso
     */
    public void setImageBar( ImageIcon image )
    {
        imageBar = image;

        if( imageBar != null )
        {
            int wBar = imageBar.getIconWidth( );
            int hBar = imageBar.getIconHeight( );
            setPreferredSize( new Dimension( wBar, hBar ) );
        }
    }

    /**
     * Establece la imagen puntero de la barra de progreso
     *
     * @param image Imagen puntero de la barra de progreso
     */
    public void setImageCursor( ImageIcon image )
    {
        imageIcon = image;
    }

    /**
     * Establece el progreso del ProgressBar.<br>
     * Se pinta automaticamente el progreso ingresado.<br>
     * Se atienden en orden de llegada, agregando el requerimiento a la cola
     *
     * @param pProgress Porcentaje de la barra de progreso. pProgress <= 100
     * @param timeDelay Delay con el que el Cursor y la barra de progreso irán avanzando
     */
    public void setProgress( final long pProgress, final long timeDelay )
    {
        ThreadProgreso thProgreso = new ThreadProgreso( pProgress, timeDelay );

        if( !threadActive )
        {
            // System.out.println( "started" );
            thProgreso.start( );
        }
        else
        {
            // System.out.println( "onQueue" );
            threadsOnQueue.add( thProgreso );
        }
    }

    /**
     * Realiza el desplazamiento del Cursor y barra de progreso con el delay dado por parámetro
     *
     * @param pProgress Progreso que se pintará
     * @param timeDelay Delay con el que el Cursor y la barra de progreso irán avanzando
     */
    private void setProgressBar( double pProgress, long timeDelay )
    {
        timeDelay = ( timeDelay == -1 ) ? 50 : timeDelay;

        double temp = pProgress > 100 ? 100 : pProgress;

        if( progress != temp )
        {
            if( temp >= progress ) // Recorrido Positivo
            {
                for( double i = progress; i <= temp; i++ )
                {
                    progress = i;
                    repaint( );
                    try
                    {
                        Thread.sleep( timeDelay );
                    }
                    catch( InterruptedException e )
                    {
                        e.printStackTrace( );
                    }
                }
            }
            else // Recorrido negativo
            {
                for( double i = progress; i >= temp; i-- )
                {
                    progress = i;
                    repaint( );
                    try
                    {
                        Thread.sleep( timeDelay );
                    }
                    catch( InterruptedException e )
                    {
                        e.printStackTrace( );
                    }
                }
            }
        }
        else if( ( temp == 0 ) && ( progress == 0 ) )
        {
            repaint( );
        }
        threadActive = false;
    }
}