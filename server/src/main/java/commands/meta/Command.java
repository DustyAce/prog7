package commands.meta;

/**
 * Command interface
 */
public interface Command {
    /**
     * Execute the command
     *
     * @param ca command arguments
     */
    public void execute(CommandArgs ca);
    public default void execute(String... args) { execute(new CommandArgs(args, null)); }

    /**
     * Get a command's description
     * @return description
     */
    public String desc();
    public String getName();

}