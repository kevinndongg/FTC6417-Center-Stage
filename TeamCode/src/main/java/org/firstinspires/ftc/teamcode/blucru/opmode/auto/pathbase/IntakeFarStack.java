package org.firstinspires.ftc.teamcode.blucru.opmode.auto.pathbase;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.blucru.common.commandbase.subsystemcommand.intake.DropdownCommand;
import org.firstinspires.ftc.teamcode.blucru.common.commandbase.systemcommand.IntakeCommand;
import org.firstinspires.ftc.teamcode.blucru.common.path.PIDPathBuilder;
import org.firstinspires.ftc.teamcode.blucru.common.states.Field;
import org.firstinspires.ftc.teamcode.blucru.common.states.Globals;

import java.lang.annotation.IncompleteAnnotationException;

public class IntakeFarStack extends PIDPathBuilder {
    public IntakeFarStack(int stackHeight, double xIncrement, double yIncrement) {
        super();
        this.setPower(0.5)
                .schedule(new IntakeCommand(stackHeight, 1))
                .addMappedPoint(Field.INTAKE_X - xIncrement, 12, 180, 2)
                .schedule(new SequentialCommandGroup(
                        new WaitCommand(300),
                        new IntakeCommand(stackHeight-1)
                ))
                .waitMillis(800)
                .setPower(0.35)
                .schedule(new SequentialCommandGroup(
                        new WaitCommand(300),
                        new IntakeCommand(0)
                ))
                .addMappedPoint(Field.INTAKE_X, 20 + yIncrement, 160, 2.5)
                .waitMillis(400);
    }

    public IntakeFarStack() {
        this(Globals.stackCenterPixels, 0, 0);
    }
}
