package tcissues;

import java.util.ArrayList;

import tcissues.issues.*;
import tcissues.resources.Colors;
import tcissues.resources.Exec;
import tcissues.resources.Images;
import totalcross.io.IOException;
import totalcross.ui.Button;
import totalcross.ui.Control;
import totalcross.ui.ImageControl;
import totalcross.ui.Label;
import totalcross.ui.MainWindow;
import totalcross.ui.PopupMenu;
import totalcross.ui.ScrollContainer;
import totalcross.ui.Window;
import totalcross.ui.dialog.InputBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.PressListener;
import totalcross.ui.image.ImageException;

public class TCIssuesHome extends ScrollContainer {
	private ArrayList<BaseIssue> issues;
	private ArrayList<BaseIssue> closedIssues;


	public TCIssuesHome() {
		issues = new ArrayList<BaseIssue>();
		issues.add(new Issue_290());
		issues.add(new Issue_310());
		issues.add(new Issue_316());
		issues.add(new Issue_320());
		issues.add(new Issue_331());
		issues.add(new Issue_336());
		issues.add(new Issue_346());
		issues.add(new Issue_347());
		issues.add(new Issue_350());
		issues.add(new Issue_351());
		issues.add(new Issue_357());
		issues.add(new Issue_369());
		issues.add(new Issue_378());
		issues.add(new Issue_379());
		issues.add(new Issue_380());
		issues.add(new Issue_381());
		issues.add(new Issue_384());
		issues.add(new Issue_388());
		issues.add(new Issue_389());
		issues.add(new Issue_393());
		issues.add(new Issue_395());
		issues.add(new Issue_397());
		issues.add(new Issue_398());
		issues.add(new Issue_401());
		issues.add(new Issue_402());
		issues.add(new Issue_408());
		issues.add(new Issue_409());
		issues.add(new Issue_410());
		issues.add(new Issue_426());
		issues.add(new Issue_436());
		issues.add(new Issue_437());
		issues.add(new Issue_429());
		issues.add(new Issue_441());
		issues.add(new Issue_442());
		issues.add(new Issue_455());
		issues.add(new Issue_467());
		issues.add(new Issue_469());
		issues.add(new Issue_470());
		issues.add(new Issue_474());
		issues.add(new Issue_476());
		issues.add(new Issue_479());
		issues.add(new Issue_480());
		issues.add(new Issue_485());
		issues.add(new Issue_486());
		issues.add(new Issue_487());
		issues.add(new Issue_488());
		issues.add(new Issue_489());
		issues.add(new Issue_497());
		issues.add(new Issue_498());
		issues.add(new Issue_502());
		issues.add(new Issue_505());
		issues.add(new Issue_506());
		issues.add(new Issue_509());
		issues.add(new Issue_512());
		issues.add(new Issue_532());
		issues.add(new Issue_540());
		issues.add(new Issue_543());
		closedIssues = new ArrayList<BaseIssue>();
		for (int i = issues.size() - 1; i >= 0; i--) {
			if (!issues.get(i).isOpen()) {
				closedIssues.add(issues.get(i));
				issues.remove(i);
			}
		}
		
		issues.sort(new BaseIssueComparator());
		closedIssues.sort(new BaseIssueComparator());
	}
	
	@Override
	public void initUI() {
		setBackColor(Colors.PRIMARY);

		ImageControl ic = new ImageControl(Images.getTotalCrossLogo());
		ic.scaleToFit = true;
		ic.centerImage = true;
		add(ic, CENTER, TOP + 50, PARENTSIZE, PARENTSIZE + 20);
		
		Button goToGitlab = new Button("GitLab Website");
		goToGitlab.setBackForeColors(Colors.BLUE, Colors.WHITE);
		goToGitlab.addPressListener(new PressListener() {
			@Override
			public void controlPressed(ControlEvent arg0) {
				Exec.openUrl("https://gitlab.com/totalcross/TotalCross/issues");
			}
		});
		add(goToGitlab, CENTER, AFTER + 50, 280 + DP, 40 + DP);
		
		Button openIssue = new Button("Open Issue by ID");
		openIssue.setBackForeColors(Colors.BLUE, Colors.WHITE);
		openIssue.addPressListener(new PressListener() {
			@Override
			public void controlPressed(ControlEvent arg0) {
				InputBox ib = new InputBox("Open Issue", "Type the issue number", "");
				ib.transitionEffect = TRANSITION_NONE;
				ib.popup();

				if (ib.getPressedButtonIndex() == 0) {
					try {
						int issueNumber = Integer.parseInt(ib.getValue());
						if (issueNumber < 1) {
							return;
						}
						
						Exec.openUrl("https://gitlab.com/totalcross/TotalCross/issues/" + issueNumber);
					} catch (Exception e) {
						// silent exception
					}
				}
			}
		});
		add(openIssue, CENTER, AFTER, 280 + DP, 40 + DP);
		
		Button changeStyle = new Button("Change Style");
		changeStyle.setBackForeColors(Colors.BLUE, Colors.WHITE);
		changeStyle.addPressListener(new PressListener() {
			@Override
			public void controlPressed(ControlEvent arg0) {
				String popdata[] = { "Flat (deprecated)", "Vista (deprecated)", "Android (deprecated)", "Holo", "Material" };

				PopupMenu popmenu;
				try {
					popmenu = new PopupMenu("Style", popdata);
					PopupMenu.cancelString = "cancel";
					popmenu.enableCancel = true;
					popmenu.resize();
					popmenu.popup();
					
					byte style = (byte)(popmenu.getSelectedIndex() + 2);
					if (style < 2 || style > 6) {
						style = 6; // material
					}
							
					Control.resetStyle();
					MainWindow.getMainWindow().setUIStyle(style);
					Window.needsPaint = true;
				} catch (IOException | ImageException e) {
					e.printStackTrace();
				}
			}
		});
		add(changeStyle, CENTER, AFTER, 280 + DP, 40 + DP);
		
		add(new Label("Open Issues"), CENTER, AFTER + 20);
		for (BaseIssue issue : issues) {
			add(new IssueButton(issue), CENTER, AFTER, 280 + DP, 40 + DP);
		}
		
		add(new Label("Closed Issues"), CENTER, AFTER + 20);
		for (BaseIssue issue : closedIssues) {
			add(new IssueButton(issue), CENTER, AFTER, 280 + DP, 40 + DP);
		}
		
		add(new Label("No More Issues"), CENTER, AFTER + 20);
	}
	
	private class IssueButton extends Button implements PressListener {
		private BaseIssue issue;
		public IssueButton(BaseIssue issue) {
			super(issue.getIssuePresentableName());
			this.issue = issue;
			this.addPressListener(this);
			if (issue.isOpen()) {
				this.setBackForeColors(Colors.BLUE, Colors.WHITE);
			} else {
				this.setBackForeColors(Colors.WHITE, Colors.BLACK);
			}
		}
		
		@Override
		public void controlPressed(ControlEvent e) {
			this.issue.ShowIssue();
		}
	}
}
