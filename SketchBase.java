//****************************************************************************

// SketchBase.  
//****************************************************************************
// Comments : 
//   Subroutines to manage and draw points, lines an triangles
//
// History :
//   Aug 2014 Created by Jianming Zhang (jimmie33@gmail.com) based on code by
//   Stan Sclaroff (from CS480 '06 poly.c)
//	 Sep 2014 Edited by Tania Papandrea (taniap@bu.edu) to implement drawLine, 
//   drawTriangle and triangleTextureMap

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SketchBase 
{

public SketchBase()
{
// deliberately left blank
}

// draw a point
public static void drawPoint(BufferedImage buff, Point2D p)
{
buff.setRGB(p.x, buff.getHeight()-p.y-1, p.c.getBRGUint8());
}

// draws a line segment using Bresenham's line algorithm
public static void drawLine(BufferedImage buff, Point2D p1, Point2D p2)
{
	int start_x = p1.x;
	int end_x = p2.x;
	int start_y = p1.y;
	int end_y = p2.y;
	float start_r = p1.c.r;
	float end_r = p2.c.r;
	float start_g = p1.c.g;
	float end_g = p2.c.g;
	float start_b = p1.c.b;
	float end_b = p2.c.b;
	float m = ((float)(end_y - start_y)/(float)(end_x - start_x));
	boolean going_up = (end_y < start_y);
	boolean going_right = (end_x > start_x);
	boolean high_slope = (Math.abs(m)>1);
	if (!high_slope && going_right) {
		float e=0;
		int x = start_x;
		int y = start_y;
		float duration = end_x - start_x;
		float delta_r = (end_r - start_r)/duration;
		float delta_g = (end_g - start_g)/duration;
		float delta_b = (end_b - start_b)/duration;
		ColorType c = new ColorType(start_r, start_g, start_b);
		while (x <= end_x) {
			Point2D p = new Point2D(x, y, c);
			drawPoint(buff, p);
			x++;
			c.r += delta_r;
			c.g += delta_g;
			c.b += delta_b;
			if (e+Math.abs(m)<0.5) {
				e = e+Math.abs(m);
			} else {
				if (going_up) {
					y--;
				} else {
					y++;
				}
				e = e+Math.abs(m)-1;
			}	
		}
	} else if (!high_slope && !going_right) {
			float e=0;
			int x = start_x;
			int y = start_y;
			float duration = end_x - start_x;
			float delta_r = (end_r - start_r)/Math.abs(duration);
			float delta_g = (end_g - start_g)/Math.abs(duration);
			float delta_b = (end_b - start_b)/Math.abs(duration);
			ColorType c = new ColorType(start_r, start_g, start_b);
			while (x >= end_x) {
				Point2D p = new Point2D(x, y, c);
				drawPoint(buff, p);
				x--;
				c.r += delta_r;
				c.g += delta_g;
				c.b += delta_b;
				if (e+Math.abs(m)<0.5) {
					e = e+Math.abs(m);
				} else {
					if (going_up) {
						y--;
					} else {
						y++;
					}
					e = e+Math.abs(m)-1;
				}	
			}
	} else if (high_slope && !going_up) {
		float e=0;
		int x = start_x;
		int y = start_y;
		float duration = end_y - start_y;
		float delta_r = (end_r - start_r)/Math.abs(duration);
		float delta_g = (end_g - start_g)/Math.abs(duration);
		float delta_b = (end_b - start_b)/Math.abs(duration);
		ColorType c = new ColorType(start_r, start_g, start_b);
		while (y <= end_y) {
			Point2D p = new Point2D(x, y, c);
			drawPoint(buff, p);
			y++;
			c.r += delta_r;
			c.g += delta_g;
			c.b += delta_b;
			if (e+(1/Math.abs(m))<0.5) {
				e = e+(1/Math.abs(m));
			} else {
				if (going_right) {
					x++;
				} else {
					x--;
				}
				e = e+(1/Math.abs(m))-1;
			}	
		}
	} else if (high_slope && going_up) {
		float e=0;
		int x = start_x;
		int y = start_y;
		float duration = end_y - start_y;
		float delta_r = (end_r - start_r)/Math.abs(duration);
		float delta_g = (end_g - start_g)/Math.abs(duration);
		float delta_b = (end_b - start_b)/Math.abs(duration);
		ColorType c = new ColorType(start_r, start_g, start_b);
		while (y >= end_y) {
			Point2D p = new Point2D(x, y, c);
			drawPoint(buff, p);
			y--;
			c.r += delta_r;
			c.g += delta_g;
			c.b += delta_b;
			if (e+(1/Math.abs(m))<0.5) {
				e = e+(1/Math.abs(m));
			} else {
				if (going_right) {
					x++;
				} else {
					x--;
				}
				e = e+(1/Math.abs(m))-1;
			}	
		}
	}
}

private static Point2D Point2D(int x, int y, ColorType c) {
	// TODO Auto-generated method stub
	return null;
}

// draws a triangle using drawLine
public static void drawTriangle(BufferedImage buff, Point2D p1, Point2D p2, Point2D p3, boolean do_smooth)
{
	int x1, x2, x3, y1, y2, y3;
	ColorType c = p1.c;
	
	//for regular triangles
	if (p1.y!=p2.y && p1.y!=p3.y && p2.y!=p3.y && p1.x!=p2.x && p1.x!=p3.x && p2.x!=p3.x) {
		//order vertices so that bottom is 1, middle is 2, and top is 3
		Point2D bottom, middle, top;
		if ((p1.y < p2.y) && (p2.y < p3.y)) {
			bottom = p3;
			middle = p2;
			top = p1;
		} else if ((p2.y < p1.y) && (p1.y < p3.y)) {
			bottom = p3;
			middle = p1;
			top = p2;
		} else 	if ((p3.y < p2.y) && (p2.y < p1.y)) {
			bottom = p1;
			middle = p2;
			top = p3;
		} else if ((p2.y < p3.y) && (p3.y < p1.y)) {
			bottom = p1;
			middle = p3;
			top = p2;
		} else if ((p1.y < p3.y) && (p3.y < p2.y)) {
			bottom = p2;
			middle = p3;
			top = p1;
		} else {
			bottom = p2;
			middle = p1;
			top = p3;
		}
		
		x1 = bottom.x;
		y1 = bottom.y;
		x2 = middle.x;
		y2 = middle.y;
		x3 = top.x;
		y3 = top.y;
		float r_1= bottom.c.r;
		float g_1 = bottom.c.g;
		float b_1 = bottom.c.b;
		float r_2= middle.c.r;
		float g_2 = middle.c.g;
		float b_2 = middle.c.b;
		float r_3= top.c.r;
		float g_3 = top.c.g;
		float b_3 = top.c.b;
		
		//calculate slopes of all lines
		float m13 = ((float)(y3 - y1)/(float)(x3 - x1));
		float m12 = ((float)(y2 - y1)/(float)(x2 - x1));
		float m23 = ((float)(y3 - y2)/(float)(x3 - x2));
		
		//fill in the bottom half
		for (int y = y1; y>=y2; y--){
			int startx = Math.round((y-y1+m12*x1)/m12);
			int endx = Math.round((y-y1+m13*x1)/m13);
			if (do_smooth) {
				float duration_12 = y1-y2;
				float delta_r12 = (r_2 - r_1)/duration_12;
				float delta_g12 = (g_2 - g_1)/duration_12;
				float delta_b12 = (b_2 - b_1)/duration_12;
				float start_r = r_1 + delta_r12*(y1-y);
				float start_g = g_1 + delta_g12*(y1-y);
				float start_b = b_1 + delta_b12*(y1-y);
				float duration_13 = y1-y3;
				float delta_r13 = (r_3 - r_1)/duration_13;
				float delta_g13 = (g_3 - g_1)/duration_13;
				float delta_b13 = (b_3 - b_1)/duration_13;
				float end_r = r_1 + delta_r13*(y1-y);
				float end_g = g_1 + delta_g13*(y1-y);
				float end_b = b_1 + delta_b13*(y1-y);
				ColorType startc = new ColorType(start_r, start_g, start_b);
				ColorType endc = new ColorType(end_r, end_g, end_b);
				drawLine(buff, new Point2D(startx,y,startc), new Point2D(endx,y,endc));				
			} else {
				drawLine(buff, new Point2D(startx,y,c), new Point2D(endx,y,c));
			}
		}
		//fill in the top half
		for (int y = y2-1; y>=y3; y--) {
			if (do_smooth) {
				float duration_23 = y2-y3;
				float delta_r23 = (r_3 - r_2)/duration_23;
				float delta_g23 = (g_3 - g_2)/duration_23;
				float delta_b23 = (b_3 - b_2)/duration_23;
				float start_r = r_2 + delta_r23*(y2-1-y);
				float start_g = g_2 + delta_g23*(y2-1-y);
				float start_b = b_2 + delta_b23*(y2-1-y);
				float duration_13 = y1-y3;
				float delta_r13 = (r_3 - r_1)/duration_13;
				float delta_g13 = (g_3 - g_1)/duration_13;
				float delta_b13 = (b_3 - b_1)/duration_13;
				float end_r = r_1 + delta_r13*(-1-y+y1);
				float end_g = g_1 + delta_g13*(-1-y+y1);
				float end_b = b_1 + delta_b13*(-1-y+y1);
				ColorType startc = new ColorType(start_r, start_g, start_b);
				ColorType endc = new ColorType(end_r, end_g, end_b);
				drawLine(buff, new Point2D(Math.round((y-y2+m23*x2)/m23),y,startc), new Point2D(Math.round((y-y1+m13*x1)/m13),y,endc));	
			} else {
				drawLine(buff, new Point2D(Math.round((y-y2+m23*x2)/m23),y,c), new Point2D(Math.round((y-y1+m13*x1)/m13),y,c));
			}
		}
	}
	//special case triangles
	else if (p1.x==p2.x || p2.x==p3.x || p1.x==p3.x) {
		//vertical line
		if (p1.y==p2.y || p2.y==p3.y || p1.y==p3.y) {
			//right angle
			int rx, ry, sx, sy, lx, ly;
			Point2D right, shortside, longside;
			if (p1.x==p2.x && p1.y==p3.y) {
				right=p1;
				shortside = p2;
				longside = p3;
			} else if (p1.x==p2.x && p2.y==p3.y) {
				right = p2;
				shortside = p1;
				longside = p3;
			} else if (p2.x==p3.x && p1.y==p3.y) {
				right = p3;
				shortside = p2;
				longside = p1;
			} else {
				right = p2;
				shortside = p3;
				longside = p1;
			}
			
			rx=right.x;
			ry=right.y;
			sx=shortside.x;
			sy=shortside.y;
			lx=longside.x;
			ly=longside.y;
			float r_r= right.c.r;
			float g_r = right.c.g;
			float b_r = right.c.b;
			float r_s= shortside.c.r;
			float g_s = shortside.c.g;
			float b_s = shortside.c.b;
			float r_l= longside.c.r;
			float g_l = longside.c.g;
			float b_l = longside.c.b;
			
			//calculate slope
			float m = ((float)(ly - sy)/(float)(lx - sx));
			
			//fill in triangle
			if (ry>ly) {
				for (int y=ry; y>=ly; y--) {
					if (do_smooth) {
						float duration = ry-ly;
						float delta_rrl = (r_l - r_r)/duration;
						float delta_grl = (g_l - g_r)/duration;
						float delta_brl = (b_l - b_r)/duration;
						float start_r = r_r + delta_rrl*(ry-y);
						float start_g = g_r + delta_grl*(ry-y);
						float start_b = b_r + delta_brl*(ry-y);
						float delta_rsl = (r_l - r_s)/duration;
						float delta_gsl = (g_l - g_s)/duration;
						float delta_bsl = (b_l - b_s)/duration;
						float end_r = r_s + delta_rsl*(ry-y);
						float end_g = g_s + delta_gsl*(ry-y);
						float end_b = b_s + delta_bsl*(ry-y);
						ColorType startc = new ColorType(start_r, start_g, start_b);
						ColorType endc = new ColorType(end_r, end_g, end_b);
						drawLine(buff, new Point2D(rx,y,startc), new Point2D(Math.round((y-sy+m*sx)/m),y,endc));	
					} else {
						drawLine(buff, new Point2D(rx,y,c), new Point2D(Math.round((y-sy+m*sx)/m),y,c));
					}
				}
			} else {
				for (int y=ry; y<=ly; y++) {
					if (do_smooth) {
						float duration = ry-ly;
						float delta_rrl = (r_l - r_r)/duration;
						float delta_grl = (g_l - g_r)/duration;
						float delta_brl = (b_l - b_r)/duration;
						float start_r = r_r + delta_rrl*(y-ry);
						float start_g = g_r + delta_grl*(y-ry);
						float start_b = b_r + delta_brl*(y-ry);
						float delta_rsl = (r_l - r_s)/duration;
						float delta_gsl = (g_l - g_s)/duration;
						float delta_bsl = (b_l - b_s)/duration;
						float end_r = r_s + delta_rsl*(y-ry);
						float end_g = g_s + delta_gsl*(y-ry);
						float end_b = b_s + delta_bsl*(y-ry);
						ColorType startc = new ColorType(start_r, start_g, start_b);
						ColorType endc = new ColorType(end_r, end_g, end_b);
						drawLine(buff, new Point2D(rx,y,startc), new Point2D(Math.round((y-sy+m*sx)/m),y,endc));	
					} else {
					drawLine(buff, new Point2D(rx,y,c), new Point2D(Math.round((y-ly+m*lx)/m),y,c));
				}
			}
		}
	} else {
		
		//vertical line, but not a right triangle
		Point2D bottom, middle, top;
		
		if (p1.x==p2.x) {
			middle = p3;
			if (p1.y>p2.y) {
				bottom = p1;
				top = p2;
			} else {
				top = p1;
				bottom = p2;
			}
		} else if (p1.x==p3.x) {
			middle = p2;
			if (p1.y>p3.y) {
				bottom = p1;
				top = p3;
			} else {
				top = p1;
				bottom = p3;
			}
		} else {
			middle = p1;
			if (p3.y>p2.y) {
				bottom = p3;
				top = p2;
			} else {
				top = p3;
				bottom = p2;
			}
		}
		
		x1 = bottom.x;
		y1 = bottom.y;
		x2 = middle.x;
		y2 = middle.y;
		x3 = top.x;
		y3 = top.y;
		float r_1= bottom.c.r;
		float g_1 = bottom.c.g;
		float b_1 = bottom.c.b;
		float r_2= middle.c.r;
		float g_2 = middle.c.g;
		float b_2 = middle.c.b;
		float r_3= top.c.r;
		float g_3 = top.c.g;
		float b_3 = top.c.b;
		
		//calculate slope
		float m12 = ((float)(y2 - y1)/(float)(x2 - x1));
		float m23 = ((float)(y3 - y2)/(float)(x3 - x2));
		
		//fill in the bottom half
		for (int y = y1; y>=y2; y--){
			int endx = Math.round((y-y1+m12*x1)/m12);
			int startx = x1;
			if (do_smooth) {
				float duration_12 = y1-y2;
				float delta_r12 = (r_2 - r_1)/duration_12;
				float delta_g12 = (g_2 - g_1)/duration_12;
				float delta_b12 = (b_2 - b_1)/duration_12;
				float start_r = r_1 + delta_r12*(y1-y);
				float start_g = g_1 + delta_g12*(y1-y);
				float start_b = b_1 + delta_b12*(y1-y);
				float duration_13 = y1-y3;
				float delta_r13 = (r_3 - r_1)/duration_13;
				float delta_g13 = (g_3 - g_1)/duration_13;
				float delta_b13 = (b_3 - b_1)/duration_13;
				float end_r = r_1 + delta_r13*(y1-y);
				float end_g = g_1 + delta_g13*(y1-y);
				float end_b = b_1 + delta_b13*(y1-y);
				ColorType startc = new ColorType(start_r, start_g, start_b);
				ColorType endc = new ColorType(end_r, end_g, end_b);
				drawLine(buff, new Point2D(startx,y,endc), new Point2D(endx,y,startc));				
			} else {
				drawLine(buff, new Point2D(startx,y,c), new Point2D(endx,y,c));
			}
		}
		//fill in the top half
		for (int y = y2-1; y>=y3; y--) {
			if (do_smooth) {
				float duration_23 = y2-y3;
				float delta_r23 = (r_3 - r_2)/duration_23;
				float delta_g23 = (g_3 - g_2)/duration_23;
				float delta_b23 = (b_3 - b_2)/duration_23;
				float start_r = r_2 + delta_r23*(y2-1-y);
				float start_g = g_2 + delta_g23*(y2-1-y);
				float start_b = b_2 + delta_b23*(y2-1-y);
				float duration_13 = y1-y3;
				float delta_r13 = (r_3 - r_1)/duration_13;
				float delta_g13 = (g_3 - g_1)/duration_13;
				float delta_b13 = (b_3 - b_1)/duration_13;
				float end_r = r_1 + delta_r13*(-1-y+y1);
				float end_g = g_1 + delta_g13*(-1-y+y1);
				float end_b = b_1 + delta_b13*(-1-y+y1);
				ColorType startc = new ColorType(start_r, start_g, start_b);
				ColorType endc = new ColorType(end_r, end_g, end_b);
				drawLine(buff, new Point2D(x1,y,endc), new Point2D(Math.round((y-y2+m23*x2)/m23),y,startc));	
			} else {
				drawLine(buff, new Point2D(x1,y,c), new Point2D(Math.round((y-y2+m23*x2)/m23),y,c));
			}
		}
	}
	}
}

//texture mapping
public static void triangleTextureMap(BufferedImage buff, BufferedImage texture, Point2D p1, Point2D p2, Point2D p3)
{
	//define u,v coordinates
	p1.u = (float)0;
	p1.v = (float)0;
	p2.u = (float)0.5;
	p2.v = (float)1;
	p3.u = (float)1;
	p3.v = (float)0;
	int x1, x2, x3, y1, y2, y3;
	int texture_height = texture.getHeight();
	int texture_width = texture.getWidth();
	
	//regular triangles
	if (p1.y!=p2.y && p1.y!=p3.y && p2.y!=p3.y && p1.x!=p2.x && p1.x!=p3.x && p2.x!=p3.x) {
		//order vertices so that bottom is 1, middle is 2, and top is 3
		Point2D bottom, middle, top;
		if ((p1.y < p2.y) && (p2.y < p3.y)) {
			bottom = p3;
			middle = p2;
			top = p1;
		} else if ((p2.y < p1.y) && (p1.y < p3.y)) {
			bottom = p3;
			middle = p1;
			top = p2;
		} else 	if ((p3.y < p2.y) && (p2.y < p1.y)) {
			bottom = p1;
			middle = p2;
			top = p3;
		} else if ((p2.y < p3.y) && (p3.y < p1.y)) {
			bottom = p1;
			middle = p3;
			top = p2;
		} else if ((p1.y < p3.y) && (p3.y < p2.y)) {
			bottom = p2;
			middle = p3;
			top = p1;
		} else {
			bottom = p2;
			middle = p1;
			top = p3;
		}
		
		x1 = bottom.x;
		y1 = bottom.y;
		x2 = middle.x;
		y2 = middle.y;
		x3 = top.x;
		y3 = top.y;
		
		//calculate slopes of all lines
		float m13 = ((float)(y3 - y1)/(float)(x3 - x1));
		float m12 = ((float)(y2 - y1)/(float)(x2 - x1));
		float m23 = ((float)(y3 - y2)/(float)(x3 - x2));
		
		float udelta_13 = (top.u - bottom.u)/(Math.abs(y1-y3));
		float vdelta_13 = (top.v - bottom.v)/(Math.abs(y1-y3));
		float udelta_12 = (middle.u - bottom.u)/(Math.abs(y1-y2));
		float vdelta_12 = (middle.v - bottom.v)/(Math.abs(y1-y2));
		float udelta_23 = (top.u - middle.u)/(Math.abs(y2-y3));
		float vdelta_23 = (top.v - middle.v)/(Math.abs(y2-y3));
		
		//fill in the bottom half
		for (int y = y1; y>=y2; y--){
			int startx = Math.round((y-y1+m12*x1)/m12);
			int endx = Math.round((y-y1+m13*x1)/m13);
			float start_u = bottom.u + udelta_12*(y1-y);
			float start_v = bottom.v + vdelta_12*(y1-y);
			float end_u = bottom.u + udelta_13*(y1-y);
			float end_v = bottom.v + vdelta_13*(y1-y);
			float u_delta = (end_u - start_u)/(Math.abs(endx-startx));
			float v_delta = (end_v - start_v)/(Math.abs(endx-startx));
			if (startx>endx) {
				for (int x = startx; x>endx; x--) {
					float u = start_u + u_delta*(Math.abs(x-startx));
					float v = start_v + v_delta*(Math.abs(x-startx));
					int r = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>16 & 0xFF);
					int g = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>8 & 0xFF);
					int b = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1)))    ) & 0xFF);
					ColorType uv = new ColorType(0,0,0);
					uv.setR_int(r);
					uv.setG_int(g);
					uv.setB_int(b);
					Point2D p = new Point2D(x, y, uv);
					drawPoint(buff, p);	
				}
			} else {
				for (int x = startx; x<=endx; x++) {
					float u = start_u + u_delta*(Math.abs(x-startx));
					float v = start_v + v_delta*(Math.abs(x-startx));
					int r = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>16 & 0xFF);
					int g = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>8 & 0xFF);
					int b = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1)))    ) & 0xFF);
					ColorType uv = new ColorType(0,0,0);
					uv.setR_int(r);
					uv.setG_int(g);
					uv.setB_int(b);
					Point2D p = new Point2D(x, y, uv);
					drawPoint(buff, p);	
				}
			}
		}
		//fill in the top half
		for (int y = y2; y>=y3; y--){
			int startx = Math.round((y-y1+m13*x1)/m13);
			int endx = Math.round((y-y2+m23*x2)/m23);
			float start_u = bottom.u + udelta_13*(y2-y +(y1-y2));
			float start_v = bottom.v + vdelta_13*(y2-y+(y1-y2));
			float end_u = middle.u + udelta_23*(y2-y);
			float end_v = middle.v + vdelta_23*(y2-y);
			float u_delta = (end_u - start_u)/(Math.abs(endx-startx));
			float v_delta = (end_v - start_v)/(Math.abs(endx-startx));
			if (startx>=endx) {
				for (int x = startx; x>=endx; x--) {	
					float u = start_u + u_delta*(Math.abs(x-startx));
					float v = start_v + v_delta*(Math.abs(x-startx));	
					int r = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>16 & 0xFF);
					int g = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>8 & 0xFF);
					int b = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1)))    ) & 0xFF);
					ColorType uv = new ColorType(0,0,0);
					uv.setR_int(r);
					uv.setG_int(g);
					uv.setB_int(b);
					Point2D p = new Point2D(x, y, uv);
					drawPoint(buff, p);	
				}
			} else {
				for (int x = startx; x<endx; x++) {	
					float u = start_u + u_delta*(Math.abs(x-startx));
					float v = start_v + v_delta*(Math.abs(x-startx));
					int r = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>16 & 0xFF);
					int g = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>8 & 0xFF);
					int b = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1)))    ) & 0xFF);
					ColorType uv = new ColorType(0,0,0);
					uv.setR_int(r);
					uv.setG_int(g);
					uv.setB_int(b);
					Point2D p = new Point2D(x, y, uv);
					drawPoint(buff, p);	
			}
			}
		}
	} else if (p1.x==p2.x || p2.x==p3.x || p1.x==p3.x) {
			//vertical line, but not a right triangle
			Point2D bottom, middle, top;
			if (p1.x==p2.x) {
				middle = p3;
				if (p1.y>p2.y) {
					bottom = p1;
					top = p2;
				} else {
					top = p1;
					bottom = p2;
				}
			} else if (p1.x==p3.x) {
				middle = p2;
				if (p1.y>p3.y) {
					bottom = p1;
					top = p3;
				} else {
					top = p1;
					bottom = p3;
				}
			} else {
				middle = p1;
				if (p3.y>p2.y) {
					bottom = p3;
					top = p2;
				} else {
					top = p3;
					bottom = p2;
				}
			}
			x1 = bottom.x;
			y1 = bottom.y;
			x2 = middle.x;
			y2 = middle.y;
			x3 = top.x;
			y3 = top.y;
			
			//calculate slope
			float m12 = ((float)(y2 - y1)/(float)(x2 - x1));
			float m23 = ((float)(y3 - y2)/(float)(x3 - x2));
			
			float udelta_13 = (top.u - bottom.u)/(y1-y3);
			float vdelta_13 = (top.v - bottom.v)/(y1-y3);
			float udelta_12 = (middle.u - bottom.u)/(y1-y2);
			float vdelta_12 = (middle.v - bottom.v)/(y1-y2);
			float udelta_23 = (top.u - middle.u)/(y2-y3);
			float vdelta_23 = (top.v - middle.v)/(y2-y3);
			
			//fill in the bottom half
			for (int y = y1; y>=y2; y--){
				int startx = Math.round((y-y1+m12*x1)/m12);
				int endx = x1;
				float start_u = bottom.u + udelta_12*(y1-y);
				float start_v = bottom.v + vdelta_12*(y1-y);
				float end_u = bottom.u + udelta_13*(y1-y);
				float end_v = bottom.v + vdelta_13*(y1-y);
				float u_delta = (end_u - start_u)/(Math.abs(endx-startx));
				float v_delta = (end_v - start_v)/(Math.abs(endx-startx));
				if (startx>endx) {
					for (int x = startx; x>=endx; x--) {
						float u = start_u + u_delta*(Math.abs(x-startx));
						float v = start_v + v_delta*(Math.abs(x-startx));
						int r = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>16 & 0xFF);
						int g = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>8 & 0xFF);
						int b = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1)))    ) & 0xFF);
						ColorType uv = new ColorType(0,0,0);
						uv.setR_int(r);
						uv.setG_int(g);
						uv.setB_int(b);
						Point2D p = new Point2D(x, y, uv);
						drawPoint(buff, p);	
					}
				} else {
					for (int x = startx; x<=endx; x++) {
						float u = start_u + u_delta*(Math.abs(x-startx));
						float v = start_v + v_delta*(Math.abs(x-startx));	
						int r = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>16 & 0xFF);
						int g = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>8 & 0xFF);
						int b = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1)))    ) & 0xFF);
						ColorType uv = new ColorType(0,0,0);
						uv.setR_int(r);
						uv.setG_int(g);
						uv.setB_int(b);
						Point2D p = new Point2D(x, y, uv);
						drawPoint(buff, p);	
					}
				}
			}
			//fill in the top half
			for (int y = y2; y>=y3; y--){
				int endx = x1;
				int startx = Math.round((y-y2+m23*x2)/m23);
				float start_u = middle.u + udelta_23*(y2-y);
				float start_v = middle.v + vdelta_23*(y2-y);
				float end_u = bottom.u + udelta_13*(y2-y +(y1-y2));
				float end_v = bottom.v + vdelta_13*(y2-y +(y1-y2));
				float u_delta = (end_u - start_u)/(Math.abs(endx-startx));
				float v_delta = (end_v - start_v)/(Math.abs(endx-startx));
				if (startx>=endx) {
					for (int x = startx; x>=endx; x--) {	
						float u = start_u + u_delta*(Math.abs(x-startx));
						float v = start_v + v_delta*(Math.abs(x-startx));	
						int r = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>16 & 0xFF);
						int g = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>8 & 0xFF);
						int b = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1)))    ) & 0xFF);
						ColorType uv = new ColorType(0,0,0);
						uv.setR_int(r);
						uv.setG_int(g);
						uv.setB_int(b);
						Point2D p = new Point2D(x, y, uv);
						drawPoint(buff, p);	
					}
				} else {
					for (int x = startx; x<=endx; x++) {	
						float u = start_u + u_delta*(Math.abs(x-startx));
						float v = start_v + v_delta*(Math.abs(x-startx));	
						int r = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>16 & 0xFF);
						int g = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1))))>>8 & 0xFF);
						int b = ((texture.getRGB(Math.round(u*(texture_width-1)), Math.round(v*(texture_height-1)))    ) & 0xFF);
						ColorType uv = new ColorType(0,0,0);
						uv.setR_int(r);
						uv.setG_int(g);
						uv.setB_int(b);
						Point2D p = new Point2D(x, y, uv);
						drawPoint(buff, p);	
					}
				}
			}
		}
	}
}